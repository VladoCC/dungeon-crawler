package com.wolg_vlad.dcrawler.math

/**
 * Created by Voyager on 06.08.2017.
 */
class DivideAction(private val dividend: MathAction, private val divider: MathAction) : MathAction() {
    override fun act(): Int {
        return dividend.act() / divider.act()
    }

    override val description: String?
        get() = "(" + dividend.description + ")" + " / " + "(" + divider.description + ")"

    override fun max(): Int {
        return dividend.max() / divider.min()
    }

    override fun min(): Int {
        return dividend.min() / divider.max()
    }

}