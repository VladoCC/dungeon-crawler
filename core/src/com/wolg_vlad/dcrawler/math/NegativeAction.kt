package com.wolg_vlad.dcrawler.math

class NegativeAction(var action: MathAction) : MathAction() {
    override fun act(): Int {
        return -action.act()
    }

    /** returns negotiated max of inner action */
    override fun max(): Int {
        return -action.max()
    }

    /** returns negotiated min of inner action */
    override fun min(): Int {
        return -action.min()
    }

    override val description: String?
        get() = "-(" + action.description + ")"

}