package ru.myitschool.dcrawler.ai;

import ru.myitschool.dcrawler.ai.task.Task;
import ru.myitschool.dcrawler.encounters.Encounter;
import ru.myitschool.dcrawler.entities.Entity;
import ru.myitschool.dcrawler.event.EntityEventAdapter;
import ru.myitschool.dcrawler.math.MathAction;

import java.util.LinkedList;

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

    public boolean handleTask(){
        Task task = tasks.peek();
        if (task == null){
            Entity.nextTurn(controlledEntity);
            return false;
        } else if (!task.isStarted()){
            task.activate();
        } else if (task.isComplete()){
            tasks.removeFirst();
            return handleTask();
        }
        return true;
    }

    public void addTask(Task task){
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
        Task task = tasks.peek();
        if (task != null) {
            task.startTurn();
        }
        handleTask();
    }

    @Override
    public void endTurn() {
        super.endTurn();
        /*Task task = tasks.peek();
        if (task != null) {
            task.endTurn();
        }
        handleTask();*/
    }

    @Override
    public void startMove() {
        super.startMove();
        Task task = tasks.peek();
        if (task != null){
            task.startMove();
        }
        handleTask();
    }

    @Override
    public void endMove() {
        super.endMove();
        Task task = tasks.peek();
        if (task != null) {
            task.endMove();
        }
        handleTask();
    }

    @Override
    public int countMp(boolean withMovement) {
        super.countMp(withMovement);
        int mp = 0;
        Task task = tasks.peek();
        if (task != null) {
            mp += task.countMp(withMovement);
        }
        handleTask();
        return mp;
    }

    @Override
    public boolean canUseSkill() {
        super.canUseSkill();
        boolean useSkill = false;
        Task task = tasks.peek();
        if (task != null) {
            useSkill = task.canUseSkill();
        }
        handleTask();
        return useSkill;
    }

    @Override
    public void startSkill() {
        super.startSkill();
        Task task = tasks.peek();
        if (task != null) {
            task.startSkill();
        }
        handleTask();
    }

    @Override
    public void endSkill() {
        super.endSkill();
        Task task = tasks.peek();
        if (task != null) {
            task.endSkill();
        }
        handleTask();
    }

    @Override
    public MathAction attackBonus(MathAction action) {
        super.attackBonus(action);
        Task task = tasks.peek();
        if (task != null) {
            action = task.attackBonus(action);
        }
        handleTask();
        return action;
    }

    @Override
    public int onDamage(int damage) {
        super.onDamage(damage);
        Task task = tasks.peek();
        if (task != null) {
            damage = task.onDamage(damage);
        }
        handleTask();
        return damage;
    }

    @Override
    public int onHeal(int heal) {
        super.onHeal(heal);
        Task task = tasks.peek();
        if (task != null) {
            heal = task.onHeal(heal);
        }
        handleTask();
        return heal;
    }

    @Override
    public int accuracyBonus(int accuracy, Entity target) {
        super.accuracyBonus(accuracy, target);
        accuracy = super.accuracyBonus(accuracy, target);
        Task task = tasks.peek();
        if (task != null) {
            accuracy = task.accuracyBonus(accuracy, target);
        }
        handleTask();
        return accuracy;
    }

    @Override
    public void onEncounter(Encounter encounter) {
        super.onEncounter(encounter);
        Task task = tasks.peek();
        if (task != null) {
            task.onEncounter(encounter);
        }
        handleTask();
    }

    @Override
    public void onDeath() {
        super.onDeath();
        super.onDeath();
        Task task = tasks.peek();
        if (task != null) {
            task.onDeath();
        }
        handleTask();
    }
}
