package net.zomis.connblocks.core

infix fun Int.fmod(other: Int) = ((this % other) + other) % other
