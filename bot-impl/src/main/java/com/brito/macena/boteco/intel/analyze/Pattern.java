package com.brito.macena.boteco.intel.analyze;

import com.brito.macena.boteco.interfaces.Analyzer;
import com.brito.macena.boteco.utils.MyHand;
import com.brito.macena.boteco.utils.Status;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;

import java.util.List;

public class Pattern extends Analyzer {

    private final GameIntel intel;
    private final TrucoCard vira;
    private final int bestCardValue;
    private final int secondBestCardValue;


    public Pattern(GameIntel intel) {
        this.intel = intel;
        vira = intel.getVira();

        MyHand myCards = new MyHand(intel.getCards(),vira);

        bestCardValue = myCards.getBestCard().relativeValue(vira);
        secondBestCardValue = myCards.getSecondBestCard().relativeValue(vira);
    }


    @Override
    public Status threeCardsHandler(List<TrucoCard> myCards) {
        return null;
    }

    @Override
    public Status twoCardsHandler(List<TrucoCard> myCards) {
        return null;
    }

    @Override
    public Status oneCardHandler() {
        return null;
    }

    private Status oneCardHandlerLosingFirstRound() {
        if (haveAtLeastOneManilha()) {
            return Status.EXCELLENT;
        }
        if (bestCardValue == 9) {
            long numberOfCardsBetterThanThree = intel.getOpenCards().stream()
                    .filter(card -> card.isManilha(vira) || card.relativeValue(vira) == 9)
                    .count();
            if (numberOfCardsBetterThanThree >= 2) {
                return Status.EXCELLENT;
            }
            return Status.GOOD;
        }
        if (bestCardValue == 8) {
            return Status.GOOD;
        }
        if (bestCardValue >= 6) {
            return Status.MEDIUM;
        }
        return Status.BAD;
    }

    private boolean haveAtLeastTwoManilhas() { return getManilhaAmount() >= 2; }

    private boolean haveAtLeastOneManilha() {
        return getManilhaAmount() >= 1;
    }

    private Status oneCardHandlerWinningFirstRound(TrucoCard oppCard, TrucoCard myCard) {
        if (intel.getHandPoints() <= 3) {
            return Status.EXCELLENT;
        }
        if (myCard.compareValueTo(oppCard, vira) > 0) {
            return Status.EXCELLENT;
        }
        return Status.BAD;
    }

    private long getManilhaAmount() {
        List<TrucoCard> myCards = intel.getCards();
        return myCards.stream()
                .filter(card -> card.isManilha(vira))
                .count();
    }

    private long powerOfTheTwoBestCards() {
        List<TrucoCard> myCards = intel.getCards();
        return myCards.stream()
                .mapToLong(card -> card.relativeValue(vira))
                .sorted()
                .limit(2)
                .sum();
    }
}
