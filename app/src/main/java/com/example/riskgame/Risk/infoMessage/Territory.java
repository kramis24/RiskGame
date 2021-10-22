package com.example.riskgame.Risk.infoMessage;
/**
 * Territory
 * Variables and methods for individual territories.
 *
 * @author Phi Nguyen, Dylan Kramis, Charlie Benning
 * @version 10/7/2021
 */

import java.util.ArrayList;

public class Territory {

    /**
     * Continent
     * Indicates which continent a territory belongs to.
     */
    public enum Continent {
        NORTH_AMERICA,
        SOUTH_AMERICA,
        EUROPE,
        AFRICA,
        ASIA,
        OCEANIA,

    }

    // instance variables
    private ArrayList<Territory> adjacents;
    private int troops;
    private Continent continent;
    private String name;
    private int owner;
    public boolean checked;

    /**
     * Default constructor for Territory.
     *
     * @param c continent territory belongs to
     * @param n name of territory
     */
    public Territory(Continent c, String n) {

        // initializes variables
        adjacents = new ArrayList<Territory>();
        continent = c;
        name = n;
        troops = 10;
        owner = 0;
        checked = false;
    }

    /**
     * Copy constructor for territory
     *
     * @param t territory being copied
     */
    public Territory(Territory t) {

        // copies variables
        this.continent = t.continent;
        this.name = t.name;
        this.owner = t.owner;
        this.troops = t.troops;
        this.adjacents = new ArrayList<Territory>();

        for(int i = 0; i < t.adjacents.size(); i++) {
            this.adjacents.add(t.adjacents.get(i));
        }
    }

    /**
     * addAdjacent
     * Adds a territory to the adjacents list.
     *
     * @param t territory being added
     */
    public void addAdjacent(Territory t) {
        adjacents.add(t);
    }

    /**
     * getOwner
     * Gets the owner of the territory.
     *
     * @return owner of territory
     */
    public int getOwner() {
        return owner;
    }

    /**
     * getTroops
     * Gets the number of troops in the territory.
     *
     * @return number of troops in territory
     */
    public int getTroops() { return troops; }

    /**
     * setTroops
     * Sets the number of troops in a territory.
     *
     * @param troops new troop count
     */
    public void setTroops(int troops) {
        this.troops = troops;
    }

    /**
     * setOwner
     * Sets the owner of a territory.
     *
     * @param owner new owner
     */
    public void setOwner(int owner) {
        this.owner = owner;
    }

    /**
     * getName
     * Gets the name of the territory.
     *
     * @return name of territory
     */
    public String getName() {
        return this.name;
    }

    /**
     * getContinent
     * Gets the continent the territory belongs to.
     *
     * @return continent of territory
     */
    public Continent getContinent() {
        return this.continent;
    }

    /**
     * getAdjacents
     * Gets the list of adjacent territories.
     *
     * @return list of adjacent territories
     */
    public ArrayList<Territory> getAdjacents() {
        return adjacents;
    }

    /**
     * equals
     * Checks if two territories are equal by name.
     *
     * @param other territory being compared
     * @return true if territories equal
     */
    public boolean equals(Territory other) {
        return this.name.equals(other.name);
    }
}
