package org.example.model.business;

import java.util.Optional;

public class PrisonEffect implements CaseEffect {

    @Override
    public Optional<String> effect(Pawn p) {
        p.setInPrison(true);
        p.setRemainingPrisonTurns(3);
        return Optional.of("Player "+p.getName()+" is in prison for three rounds");
    }
}
