package com.wolg_vlad.dcrawler.math

/**
 * Created by Voyager on 18.04.2018.
 */
interface Statement {
    val result: () -> Boolean
    val description: String
}