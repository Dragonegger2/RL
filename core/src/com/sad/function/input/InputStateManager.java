package com.sad.function.input;

import com.badlogic.gdx.Gdx;
import com.sad.function.common.SizedStack;
import com.sad.function.common.Subject;
import com.sad.function.global.Global;
import com.sad.function.input.definitions.Action;
import com.sad.function.input.definitions.Context;
import com.sad.function.input.definitions.Keyboard;
import com.sad.function.input.definitions.State;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages all states related to input.
 */
public class InputStateManager extends Subject {
    private SizedStack<Keyboard> keyboardStates = new SizedStack<>(2);

    private List<Integer> states = new ArrayList<>();
    private List<Integer> actions = new ArrayList<>();

    //The observers of this object is anybody who wants input.

    /**
     * Handle any input processors that are related to this application.
     */
    public void handleInput() {
        //Grab raw inputs.
        handleKeyboard();

        //Map them to states.
        handleStates();

        //Map them to actions.
        handleActions();

    }

    private void handleKeyboard() {
        Keyboard currentState = new Keyboard();
        for (int i = 0; i < 256; i++) {
            boolean keyPressed = Gdx.input.isKeyPressed(i);
            if (keyPressed) {
                currentState.setKeyDown(i);
            } else {
                currentState.setKeyUp(i);
            }
        }

        keyboardStates.push(currentState);
    }

    /**
     * Creates a list of keys that are currently down.
     */
    private void handleStates() {
        states.clear();        //Prevent them from queueing up when we don't need them to.

        for (int i = 0; i < 255; i++) {
            if (Gdx.input.isKeyPressed(i)) {
                states.add(i);
            }
        }
    }

    /**
     * Uses the current and a previous state to determine if a key was just pressed and then released.
     * <p>
     * Current should be up and the previous state should be down.
     */
    private void handleActions() {
        actions.clear();        //Prevent them from queueing up when we don't need them to.

        //Check for a previous state.
        for (int i = 0; i < 255; i++) {
            if (Gdx.input.isKeyJustPressed(i)) {
                System.out.println("Action triggered.");
                actions.add(i);
            }
        }
    }

    public List<Integer> getStates() {
        return states;
    }

    public List<Integer> getActions() {
        return actions;
    }
}
