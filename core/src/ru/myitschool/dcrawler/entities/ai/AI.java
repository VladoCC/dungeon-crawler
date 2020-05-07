package ru.myitschool.dcrawler.entities.ai;

import ru.myitschool.dcrawler.entities.ai.task.Task;
import ru.myitschool.dcrawler.encounters.Encounter;
import ru.myitschool.dcrawler.entities.Entity;
import ru.myitschool.dcrawler.event.EntityEventAdapter;
import ru.myitschool.dcrawler.math.MathAction;
import ru.myitschool.dcrawler.utils.functional.KeepingFunction;
import ru.myitschool.dcrawler.utils.functional.VoidFunction;

import java.util.LinkedList;
import java.util.function.Supplier;

/**
 * Created by Voyager on 08.08.2017.
 */
public abstract class AI extends EntityEventAdapter implements Cloneable {

    protected Entity controlledEntity;

    private LinkedList<Task> tasks = new LinkedList<Task>();

    public AI(Entity controlledEntity) {
        this.controlledEntity = controlledEntity;
    }

    public Entity getControlledEntity() {
        return controlledEntity;
    }

    public void setControlledEntity(Entity controlledEntity) {
        this.controlledEntity = controlledEntity;
    }

    private <R> Supplier<R> handleTask(KeepingFunction<Task, R> event){
        Task task = tasks.peek();

        // task is always started before this moment,
        // so we shouldn't worry about activation or null checks
        Supplier<R> result = event.apply(task);
        if (task.isComplete()) {
            tasks.removeFirst();
            // we want to start new task or finish turn if there is no tasks
            activateTask();
        }
        return result;
    }

    private void activateTask() {
        Task task = tasks.peek();
        if (task == null) {
            Entity.nextTurn(controlledEntity);
            return;
        }
        if (!task.isStarted()) {
            task.start();
        }
    }

    public void addTask(Task task){
        task.setEntity(controlledEntity);
        tasks.add(task);
    }

    public abstract void aiAnalyze();

    @Override
    public AI clone() {
        try {
            return (AI) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void startTurn() {
        super.startTurn();
        aiAnalyze();
        activateTask();
        handleTask(new VoidFunction<>(Task::startTurn));
    }

    @Override
    public void endTurn() {
        super.endTurn();
    }

    @Override
    public void startMove() {
        super.startMove();
        handleTask(new VoidFunction<>(Task::startMove));
    }

    @Override
    public void endMove() {
        super.endMove();
        handleTask(new VoidFunction<>(Task::endMove));
    }

    @Override
    public int countMp(boolean withMovement) {
        super.countMp(withMovement);
        return handleTask((t) -> () -> t.countMp(withMovement)).get();
    }

    @Override
    public boolean canUseSkill() {
        super.canUseSkill();
        return handleTask((t) -> t::canUseSkill).get();
    }

    @Override
    public void startSkill() {
        super.startSkill();
        handleTask(new VoidFunction<>(Task::startSkill));
    }

    @Override
    public void endSkill() {
        super.endSkill();
        handleTask(new VoidFunction<>(Task::endSkill));
    }

    @Override
    public MathAction attackBonus(MathAction action) {
        super.attackBonus(action);
        return handleTask((t) -> () -> t.attackBonus(action)).get();
    }

    @Override
    public int onDamage(int damage) {
        super.onDamage(damage);
        return handleTask((t) -> () -> t.onDamage(damage)).get();
    }

    @Override
    public int onHeal(int heal) {
        super.onHeal(heal);
        return handleTask((t) -> () -> t.onHeal(heal)).get();
    }

    @Override
    public int accuracyBonus(int accuracy, Entity target) {
        super.accuracyBonus(accuracy, target);
        return handleTask((t) -> () -> t.accuracyBonus(accuracy, target)).get();
    }

    @Override
    public void onEncounter(Encounter encounter) {
        super.onEncounter(encounter);
        handleTask(new VoidFunction<>((t) -> t.onEncounter(encounter)));
    }

    @Override
    public void onDeath() {
        super.onDeath();
        handleTask(new VoidFunction<>(Task::onDeath));
    }
}
