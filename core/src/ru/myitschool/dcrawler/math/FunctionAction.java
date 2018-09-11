package ru.myitschool.dcrawler.math;

/**
 * Created by Voyager on 09.05.2018.
 */
public class FunctionAction extends MathAction {

    Function function;
    String descripton;

    public FunctionAction(Function function, String descripton) {
        this.function = function;
        this.descripton = descripton;
    }

    @Override
    public int act() {
        return 0;
    }

    @Override
    public String getDescription() {
        return null;
    }

    public interface Function{
        int getNumber(Object[] args);
    }
}
