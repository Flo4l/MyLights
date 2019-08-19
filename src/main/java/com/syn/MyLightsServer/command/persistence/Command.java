package com.syn.MyLightsServer.command.persistence;

import com.syn.MyLightsServer.group.persistence.Group;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;

@Entity
@Table(name = "command")
public class Command {

    @Transient
    public static final char SINGLE_COLOR_MODE = 's';
    @Transient
    public static final char MULTIPLE_COLOR_MODE = 'm';
    @Transient
    public static final char FADE_COLOR_MODE = 'f';

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    private char mode;

    @NotNull
    @Min(1)
    @Max(1200)
    private int secondsToNextColor;

    @NotNull
    @OneToOne(targetEntity = Group.class, cascade = CascadeType.ALL)
    private Group group;

    @NotNull
    ArrayList<Color> colors;

    public Command() {
        this(1, Command.SINGLE_COLOR_MODE, new Group(), new ArrayList<>());
    }

    public Command(
            @NotNull @Min(1) @Max(1200) int secondsToNextColor,
            @NotNull char mode,
            @NotNull Group group,
            @NotNull ArrayList<Color> colors) {

        this.secondsToNextColor = secondsToNextColor;
        this.mode = mode;
        this.group = group;
        this.colors = colors;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSecondsToNextColor() {
        return secondsToNextColor;
    }

    public void setSecondsToNextColor(int secondsToNextColor) {
        this.secondsToNextColor = secondsToNextColor;
    }

    public char getMode() {
        return mode;
    }

    public void setMode(char mode) {
        this.mode = mode;
    }

    public ArrayList<Color> getColors() {
        return colors;
    }

    public void setColors(ArrayList<Color> colors) {
        this.colors = colors;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public String toJSON() {
        String json = "{\"command\":{";
        json += "\"mode\":\"" + mode + "\",";
        json += "\"secondsToNextColor\":" + secondsToNextColor + ",";
        json += "\"groupId\":" + group.getId() + ",";
        json += "\"colors\":[";

        for(Color c : colors) {
            json += "{\"red\":" + c.getRed() + ",";
            json += "\"green\":" + c.getGreen() + ",";
            json += "\"blue\":" + c.getBlue() + "}";
            if(colors.indexOf(c) < colors.size() - 1) {
                json += ",";
            }
        }

        json += "]}}";
        return json;
    }
}
