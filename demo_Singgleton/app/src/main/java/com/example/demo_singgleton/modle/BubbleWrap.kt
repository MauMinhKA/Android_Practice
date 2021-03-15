package com.example.demo_singgleton.modle


class BubbleWrap {



    companion object {
        private val ADD_MORE_NUMBERS = 10
        var numBubbles: Int = 0

        private var instance: BubbleWrap? = null
        operator fun invoke() = synchronized(this) {
            if (instance == null) instance = BubbleWrap()
            instance
        }

        fun addMoreBubbles() {
            numBubbles += ADD_MORE_NUMBERS
        }

        fun popBubbles() {
            numBubbles--
        }
    }

}
