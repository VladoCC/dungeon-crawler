package com.wolg_vlad.dcrawler.math

/**
 * Created by Voyager on 09.05.2018.
 */
class FunctionAction(var function: Function, var descripton: String) : MathAction() {
    override fun act(): Int {
        return 0
    }

    override val description: String?
        get() = null

    interface Function {
        fun getNumber(args: Array<Any?>?): Int
    }

}