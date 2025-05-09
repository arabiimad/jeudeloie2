package org.example.model.business;

import java.util.Optional;

/**
 * Backward effect
 */
public class BackEffect implements CaseEffect {
    @Override
    public Optional<String> effect(Pawn p) {
        p.setPosition(Math.max(p.getPosition() - 2, 0));
        return Optional.empty();
    }
}
