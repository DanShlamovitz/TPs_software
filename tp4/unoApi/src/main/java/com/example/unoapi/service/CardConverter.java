package com.example.unoapi.service;

import com.example.unoapi.model.*;
import com.example.unoapi.service.exceptions.InvalidCardException;
import org.springframework.stereotype.Service;

@Service
public class CardConverter {

    public Card jsonToCard(JsonCard jsonCard) {
        if (jsonCard == null) {
            throw new InvalidCardException("JsonCard cannot be null");
        }

        try {
            Card card = createCardByType(jsonCard);
            
            if (jsonCard.isShout()) {
                card = card.uno();
            }
            
            return card;
        } catch (Exception e) {
            throw new InvalidCardException("Failed to convert JsonCard to Card: " + e.getMessage(), e);
        }
    }

    private Card createCardByType(JsonCard jsonCard) {
        String type = jsonCard.getType();
        String color = jsonCard.getColor();
        Integer number = jsonCard.getNumber();

        if (type == null || type.trim().isEmpty()) {
            throw new InvalidCardException("Card type cannot be null or empty");
        }

        switch (type) {
            case "NumberCard":
                if (color == null || color.trim().isEmpty()) {
                    throw new InvalidCardException("NumberCard requires a valid color");
                }
                if (number == null || number < 0 || number > 9) {
                    throw new InvalidCardException("NumberCard requires a number between 0 and 9");
                }
                return new NumberCard(color, number);
            
            case "SkipCard":
                if (color == null || color.trim().isEmpty()) {
                    throw new InvalidCardException("SkipCard requires a valid color");
                }
                return new SkipCard(color);
                
            case "ReverseCard":
                if (color == null || color.trim().isEmpty()) {
                    throw new InvalidCardException("ReverseCard requires a valid color");
                }
                return new ReverseCard(color);
                
            case "Draw2Card":
                if (color == null || color.trim().isEmpty()) {
                    throw new InvalidCardException("Draw2Card requires a valid color");
                }
                return new Draw2Card(color);
                
            case "WildCard":
                WildCard wildCard = new WildCard();
                if (color != null && !color.trim().isEmpty()) {
                    wildCard = (WildCard) wildCard.asColor(color);
                }
                return wildCard;
                
            default:
                throw new InvalidCardException("Unknown card type: " + type);
        }
    }
} 