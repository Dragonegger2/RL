package com.sad.function.input;

import com.badlogic.gdx.Gdx;
import com.sad.function.common.Subject;
import com.sad.function.global.Global;
import com.sad.function.input.definitions.*;
import com.sad.function.input.keyboard.Keyboard;
import com.sad.function.input.keyboard.KeyboardState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Manages all states related to input.
 *
 * The observers of this object is anybody who wants input.
 */
public class InputStateManager extends Subject<MappedInput> {
    private KeyboardState keyboardStates = new KeyboardState(2);

    private List<Integer> states = new ArrayList<>();
    private List<Integer> actions = new ArrayList<>();

    private static final Logger logger = LogManager.getLogger(InputStateManager.class.getName());

    /**
     * Handle any input processors that are related to this application.
     */
    public void handleInput() {
        handleKeyboard();

        createActionList();
        createStateList();

        MappedInput mappedInput = mapRawInputToActions();
        getObservers().forEach(inputEventObserver -> inputEventObserver.onNotify(mappedInput));
    }

    /**
     * Polls the entire keyboard and pushes a new keyboard state onto the keyboard state stack.
     */
    private void handleKeyboard() {
        Keyboard currentState = new Keyboard();
        for (int i = 0; i < 256; i++) {
            if (Gdx.input.isKeyPressed(i)) {
                currentState.setKeyDown(i);
            } else {
                currentState.setKeyUp(i);
            }
        }

        keyboardStates.pushKeyboardState(currentState);
    }

    private MappedInput mapRawInputToActions() {
        MappedInput mappedInput = new MappedInput();

        //Loop through all active contexts.
        Context target = Global.activeContextsChain;
        while (target != null) {
            Iterator<Integer> actionIterator = actions.iterator();

            //TODO: Change this to provide full list with variable to assert on.
            //Bind all actions.
            while (actionIterator.hasNext()) {
                Integer next = actionIterator.next();

                Action ac = target.mapButtonToAction(next);
                if (ac != null) {
                    logger.info(ac);
                    mappedInput.addAction(ac);
                    actionIterator.remove();
                }
            }

            //TODO: Change this to provide full list. Bind all states.
            Iterator<Integer> stateIterator = states.iterator();
            while (stateIterator.hasNext()) {
                Integer next = stateIterator.next();

                State st = target.mapButtonToState(next);
                if (st != null) {
                    logger.info(st);
                    mappedInput.addState(st);
                    stateIterator.remove();
                }
            }
            //Increments to next context.
            target = (Context) target.getNext();
        }

        return mappedInput;
    }
    //TODO: Currently there is not binding for "raw input" to "buttons" right is that my problem?

    /**
     * Creates a list of keys that are currently down.
     *
     * TODO: Needs to supply a full list of all entities
     */
    private void createStateList() {
        states.clear();

        for (int i = 0; i < 255; i++) {
            if (Gdx.input.isKeyPressed(i)) {
                states.add(i);
            }
        }
    }

    /**
     * Uses the current and a previous state to determine if a keyStates was just pressed and then released.
     * <p>
     * Current should be up and the previous state should be down.
     */
    private void createActionList() {
        actions.clear();        //Prevent them from queueing up when we don't need them to.

        for (int i = 0; i < 255; i++) {
            if (Gdx.input.isKeyJustPressed(i)) {
                actions.add(i);
            }
        }
    }
}
