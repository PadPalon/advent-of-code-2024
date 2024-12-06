package ch.neukom.advent2024.util.inputreaders;

import ch.neukom.advent2024.util.characterMap.CharacterMapUtil;
import ch.neukom.advent2024.util.data.Position;

import java.util.Map;
import java.util.function.Function;

public class InputMapReader extends InputResourceReader {
    public InputMapReader(Class<?> clazz) {
        super(clazz);
    }

    public InputMapReader(Class<?> clazz, String filename) {
        super(clazz, filename);
    }

    public Map<Position, Character> readIntoMap() {
        return CharacterMapUtil.buildCharacterMap(this);
    }

    public <T> Map<Position, T> readIntoMap(Function<Character, T> transformer) {
        return CharacterMapUtil.buildCharacterMap(this, transformer);
    }
}
