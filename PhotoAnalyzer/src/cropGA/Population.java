/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cropGA;

import analyzer.Analyzer;

/**
 *
 * @author Nomi
 */
public class Population {
    Individual[] individuals;
    
    private Analyzer analyzer;
    

    /*
     * Constructors
     */
    // Create a population
    public Population(int populationSize, boolean initialise, int width, int height, Analyzer an) {
        analyzer = an;
        individuals = new Individual[populationSize];
        // Initialise population
        if (initialise) {
            // Loop and create individuals
            Individual newIndividual = new Individual(analyzer);
            newIndividual.generateFirstIndividual(width, height);
            saveIndividual(0, newIndividual);
            for (int i = 1; i < size(); i++) {
                newIndividual = new Individual(analyzer);
                newIndividual.generateIndividual(width, height);
                saveIndividual(i, newIndividual);
            }
        }
    }

    /* Getters */
    public Individual getIndividual(int index) {
        return individuals[index];
    }

    public Individual getFittest() {
        Individual fittest = individuals[0];
        // Loop through individuals to find fittest
        for (int i = 0; i < size(); i++) {
            if (fittest.getAestheticScore() <= getIndividual(i).getAestheticScore()) {
                fittest = getIndividual(i);
            }
        }
        return fittest;
    }

    /* Public methods */
    // Get population size
    public int size() {
        return individuals.length;
    }

    // Save individual
    public void saveIndividual(int index, Individual indiv) {
        individuals[index] = indiv;
    }
}
