package org.example.model.technical;

import org.example.model.business.*;

/**
 * Classical board generator
 */
public class ClassicalBoardGenerator implements BoardGenerator {

    @Override
    public Board generateBoard() {
        Case[] cases = new Case[25];
        for (int i = 0; i < cases.length; i++) {
            cases[i] = new Case (i);
        }

        cases[2].setCaseEffect(new BackEffect());
        cases[3].setCaseEffect(new QuestionEffect());
        cases[4].setCaseEffect(new BonusEffect());
        cases[7].setCaseEffect(new ReturnEffect());
        cases[10].setCaseEffect(new ReturnEffect());
        cases[11].setCaseEffect(new QuestionEffect());
        cases[14].setCaseEffect(new PrisonEffect());
        cases[16].setCaseEffect(new ReturnEffect());
        cases[19].setCaseEffect(new BackEffect());
        cases[20].setCaseEffect(new BonusEffect());
        cases[23].setCaseEffect(new PrisonEffect());

        return new Board(cases);
    }
}
