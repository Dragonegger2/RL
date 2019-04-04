package com.sad.function.input;

import com.sad.function.global.Global;

import java.util.List;
import java.util.Map;

public class InputMapper {

    enum RawInputButton {
        W,
        S,
        D,
        A
    }

    enum RawInputAxis {
        RAW_INPUT_AXIS_MOUSE_X,
        RAW_INPUT_AXIS_MOUSE_Y,
    }

    enum Action {
        ACTION_ONE,
    }

    enum State {
        STATE_ONE,
    }

    enum Range {
        RANGE_ONE,
    }

    public void setRawButtonState(RawInputButton button, boolean pressed, boolean previouslyPressed) {
        Action action = null;
        State state;

        if(pressed && !previouslyPressed) {
            if(mapButtonToAction(button, action)) {
                currentMappedInput.actions.add(action);
            }
        }
        else if( pressed ) {

        }
    }

    private boolean mapButtonToAction(RawInputButton button, Action action) {
        Global.gameContexts.forEach((context, inputContext) -> {
            if(inputContext.actions.contains())
        });
        return false;
    }


    MappedInput currentMappedInput = new MappedInput();

    //Essentially a context object too.
    public class MappedInput {
        List<Action> actions;
        List<State> states;
        Map<Range, Double> ranges;

        void eatAction(Action action) { actions.remove(action); }
        void eatState(State state) { states.remove(state); }
        void eatRange(Range range) {
            //TODO,
            /**
             * 			std::map<Range, double>::iterator iter = Ranges.find(range);
             * 			if(iter != Ranges.end())
             * 				Ranges.erase(iter);
             */
        }
    }
}
