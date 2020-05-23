package com.wolg_vlad.dcrawler.math

/**
 * Created by Voyager on 18.04.2018.
 */
class StatementAction(private val statement: Statement, private val trueAction: MathAction, private val falseAction: MathAction) : MathAction() {
    override fun act(): Int {
        return if (statement.result()) {
            trueAction.act()
        } else {
            falseAction.act()
        }
    }

    override val description: String?
        get() = "if(statement(" + statement.description + "), true(" + trueAction.description + "), false(" + falseAction.description + ")"

}