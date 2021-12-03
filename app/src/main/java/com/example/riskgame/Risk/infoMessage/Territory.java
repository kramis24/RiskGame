package com.example.riskgame.Risk.infoMessage;
/**
 * Territory
 * Variables and methods for individual territories.
 *
 * @author Phi Nguyen, Dylan Kramis, Charlie Benning
 * @version 12/2/2021
 */

import com.example.riskgame.GameFramework.utilities.Logger;

import java.io.Serializable;
import java.util.ArrayList;

public class Territory implements Serializable {
    private static final long serialVersionUID = 3652321012489624416L;
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
    public int turnNumChanged;

    // location variables
    private int centerX = 0;
    private int centerY = 0;
    private int boxWidth = 100;
    private int boxHeight = 100;

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
        owner = -1;
        checked = false;
        turnNumChanged = -1;
    }

    /**
     * Constructor with location coordinates
     *
     * @param c continent territory belongs to
     * @param n name of territory
     * @param x center x coordinate
     * @param y center y coordinate
     */
    public Territory(Continent c, String n, int x, int y) {

        // initializes variables
        adjacents = new ArrayList<Territory>();
        continent = c;
        name = n;
        troops = 10;
        owner = -1;
        checked = false;
        turnNumChanged = -1;
        centerX = x;
        centerY = y;
    }

    /**
     * Copy constructor for territory
     *
     * @param t territory being copied
     */
    public Territory(Territory t) {
        // copies variables
        /*this.continent = t.continent;
        this.name = t.name;
        this.owner = t.owner;
        this.troops = t.troops;
        this.centerX = t.centerX;
        this.centerY = t.centerY;
        this.checked = t.checked;
        this.highlightMoved = t.highlightMoved;
        this.adjacents = new ArrayList<Territory>();
        this.turnNumChanged = t.turnNumChanged;

        for(int i = 0; i < t.adjacents.size(); i++) {
            this.adjacents.add(t.adjacents.get(i));
        }*/
        Logger.log("Territory Copy Constructor","Unsupported due to adjacency matrix");
    }

    /**
     * Copies information from one territory to another
     *
     * @param t the territory to be copied
     */
    public void setUp(Territory t) {
        this.owner = t.owner;
        this.troops = t.troops;
        this.checked = t.checked;
        this.turnNumChanged = t.turnNumChanged;
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
     * getX
     * Gets x coordinate.
     *
     * @return x
     */
    public int getX() {
        return centerX;
    }

    /**
     * getY
     * Gets y coordinate.
     *
     * @return y
     */
    public int getY() {
        return centerY;
    }

    /**
     * getWidth
     * Gets width.
     *
     * @return width
     */
    public int getWidth() {
        return boxWidth;
    }

    /**
     * getHeight
     * Gets height,
     *
     * @return height
     */
    public int getHeight() {
        return boxHeight;
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

    // the following methods are for the advanced computer player to use
    /**
     * hasEnemyAdjacent
     * Checks if there adjacent territories held by enemy players.
     *
     * @return true if enemies are nearby
     */
    public boolean hasEnemyAdjacent() {
        for (Territory a : adjacents) {
            if (a.owner != this.owner) return true;
        }

        return false;
    }

    /**
     * getDisadvantage
     * Gets a territories worst ratio of troops to enemy troops.
     *
     * @return lowest troop to enemy troop ratio
     */
    public double getDisadvantage() {
        double ratio = -1;

        for (Territory a : adjacents) {
            if (((this.troops / a.troops < ratio) || ratio == -1) && a.owner != this.owner) {
                ratio = (double) this.troops / (double) a.troops;
            }
        }

        return ratio;
    }

    /**
     * getGreatestThreat
     * Finds the most threatening enemy territory.
     *
     * @return adjacent enemy territory with most troops
     */
    public Territory getGreatestThreat() {
        Territory greatestThreat = null;
        int maxTroops = 0;

        for (Territory a : adjacents) {
            if (a.troops > maxTroops && a.owner != this.owner) {
                greatestThreat = a;
                maxTroops = a.troops;
            }
        }

        return greatestThreat;
    }
}