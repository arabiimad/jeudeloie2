package org.example.model.business;

import java.util.Optional;

public class BonusEffect implements CaseEffect {
    @Override
    public Optional<String> effect(Pawn p) {
        p.setScore(p.getScore() + 3);
        return Optional.of("Bonus effect! Your score increases by 10 points.");
    }
}