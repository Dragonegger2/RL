package com.sad.function.input;

import com.sad.function.common.Subject;
import com.sad.function.event.Event;
import com.sad.function.global.Global;
import com.sad.function.input.definitions.Action;
import com.sad.function.input.definitions.Context;
import com.sad.function.input.definitions.State;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.sad.function.input.definitions.RawInputConstants.RawInputButton;

public class InputMapper extends Subject {
    private MappedInput currentMappedInput = new MappedInput();

    public void setRawButtonState(RawInputButton button, boolean pressed, boolean previouslyPressed) {
        if (pressed && !previouslyPressed) {
            currentMappedInput.actions.addAll(mapButtonToAction(button));
        }

        if (pressed) {
            currentMappedInput.states.addAll(mapButtonToState(button));
        }


    }

    public void clear() {
        currentMappedInput.actions.clear();
        //currentMappedInput.ranges.clear();
        // Note: we do NOT clear states, because they need to remain set
        // across frames so that they don't accidentally show "off" for
        // a tick or two while the raw input is still pending.
    }

    /**
     * Could theoretically fire multiple actions from a single button press.
     *
     * @param button raw input that is being mapped to an action, state, or range.
     * @return a list of actions that are triggered from available active contexts.
     */
    private List<Action> mapButtonToAction(RawInputButton button) {
        //List of actions triggered by this button press.
        List<Action> triggeredActions = new ArrayList<>();
        for (int i = 0; i < Global.activeContexts.size(); i++) {
            Context targetContext = Global.activeContexts.get(i);
            triggeredActions.addAll(targetContext.mapButtonToAction(button));
        }

        return triggeredActions;
    }

    /**
     * Could theoretically fire multiple actions from a single button press.
     *
     * @param button raw input that is being mapped to an action, state, or range.
     * @return a list of actions that are triggered from available active contexts.
     */
    private List<State> mapButtonToState(RawInputButton button) {
        //List of actions triggered by this button press.
        List<State> triggeredStates = new ArrayList<>();
        for (int i = 0; i < Global.activeContexts.size(); i++) {
            Context targetContext = Global.activeContexts.get(i);
            triggeredStates.addAll(targetContext.mapButtonToState(button));
        }

        return triggeredStates;
    }

    /**
     * Dispatch to all listeners the current state of input.
     */
    public void dispatch() {
        //Collect current input state.
        MappedInput input = currentMappedInput;

        getObservers().forEach(watcher -> watcher.onNotify(null, input));
    }


    public class MappedInput extends Event {
        public Set<Action> actions;
        public Set<State> states;

        public void addAction(Action action) {
            if(actions == null) {
                actions = new HashSet<>();
            }
            actions.add(action);
        }

        public void addAllActions(List<Action> actions) {
            if(this.actions == null) {
                this.actions = new HashSet<>();
            }

        }
        void eatAction(Action action) {
            actions.remove(action);
        }

        void eatState(State state) {
            states.remove(state);
        }
    }
}
