package com.sad.function.collision;

import java.util.*;

public class ContactManager {
    private final List<Contact> contactQueue;

    private Map<Integer, Contact> contacts;
    private Map<Integer, Contact> contacts1;

    public ContactManager() {
        contacts = new HashMap<>();
        contacts1 = new HashMap<>();

        contactQueue = new ArrayList<>(5);
    }

    public void addContact(Contact contact) {
        contactQueue.add(contact);
    }

    public void updateAndNotify(List<ContactListener> listeners) {
        int size = contactQueue.size();
        int listenerSize = listeners != null ? listeners.size() : 0;

        Map<Integer, Contact> newMap = this.contacts1;

        for(int i = 0; i < size; i++) {
            Contact newContact =  contactQueue.get(i);
            Contact oldContact = null;

            oldContact = contacts.remove(newContact.hashCode()); //keep removing them from contacts ensures that by the end of this we've only got those that are needed for removal at the end.

            //It's already an existing contact if it's not null.
            if(oldContact != null) {
                for(int l = 0; l < listenerSize; l++) {
                    ContactListener listener = listeners.get(l);
                    //Oh, so it uses contact points to prevent false positives on contact. Two objects can have a persisted
                    //contact but their points AKA "Contacts" can be different. I don't have that.
                    listener.persist(oldContact);
                }
            } else {
                for(int l = 0; l < listenerSize; l++) {
                    ContactListener listener = listeners.get(l);

                    //TODO: Try to ensure we have an updated list of contacts on each entity.
                    newContact.getFixture1().addContact(newContact.getFixture2());
                    newContact.getFixture2().addContact(newContact.getFixture1());
                    listener.begin(newContact);
                }
            }
            //Either create the new contact, or put it back into the contact map.
            newMap.put(newContact.hashCode(), newContact);
        }

        //If we still have any contacts left in the map these are the contacts that need to be removed.
        if(!contacts.isEmpty()) {
            Iterator<Contact> ic = contacts.values().iterator();
            while(ic.hasNext()) {
                Contact contact = ic.next();

                for(int l = 0; l < listenerSize; l++) {
                    ContactListener listener = listeners.get(l);

                    //TODO: Not sure this works. Try to ensure we have an updated list of contacts on each entity.
                    contact.getFixture1().removeContact(contact.getFixture2());
                    contact.getFixture2().removeContact(contact.getFixture1());
                    listener.end(contact);
                }

            }
        }

        //Clear the contacts and set them equal to the new map.
        if(size > 0) {
            contacts.clear();
            contacts1 = this.contacts;
            this.contacts = newMap;
        } else {
            contacts.clear();
        }

        contactQueue.clear();
    }
}
