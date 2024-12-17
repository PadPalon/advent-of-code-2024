package ch.neukom.advent2024.day17;

import ch.neukom.advent2024.util.splitter.Splitters;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class Util {
    private Util() {
    }

    static int getRegister(List<String> lines, int index) {
        String line = lines.get(index);
        return Integer.parseInt(line.substring(line.indexOf(':') + 2));
    }

    static List<Integer> getProgram(List<String> lines) {
        String programLine = lines.getLast();
        return Splitters.COMMA_SPLITTER.splitToStream(programLine.substring(programLine.indexOf(':') + 2))
            .map(Integer::parseInt)
            .toList();
    }

    public static class Computer {
        public static Set<Integer> comboOperandOpcodes = Set.of(
            0, 2, 5, 6, 7
        );

        private int instructionPointer = 0;

        private long A;
        private long B;
        private long C;

        private final Consumer<Integer> output;

        public Computer(long initialA,
                        long initialB,
                        long initialC,
                        Consumer<Integer> output) {
            this.A = initialA;
            this.B = initialB;
            this.C = initialC;
            this.output = output;
        }

        public void runProgram(List<Integer> program) {
            checkValidProgram(program);

            while (instructionPointer < program.size()) {
                Integer operand = program.get(instructionPointer + 1);
                boolean increaseInstructionPointer = switch (program.get(instructionPointer)) {
                    case 0 -> adv(operand);
                    case 1 -> bxl(operand);
                    case 2 -> bst(operand);
                    case 3 -> jnz(operand);
                    case 4 -> bxc(operand);
                    case 5 -> out(operand);
                    case 6 -> bdv(operand);
                    case 7 -> cdv(operand);
                    default -> throw new IllegalStateException();
                };
                if (increaseInstructionPointer) {
                    instructionPointer = instructionPointer + 2;
                }
            }
        }

        private boolean adv(int operand) {
            long comboOperand = getComboOperand(operand);
            A = (long) (A / Math.pow(2, comboOperand));
            return true;
        }

        private boolean bxl(int operand) {
            B = B ^ operand;
            return true;
        }

        private boolean bst(int operand) {
            long comboOperand = getComboOperand(operand);
            B = comboOperand % 8;
            return true;
        }

        private boolean jnz(int operand) {
            if (A == 0) {
                return true;
            } else {
                instructionPointer = operand;
                return false;
            }
        }

        private boolean bxc(int operand) {
            B = B ^ C;
            return true;
        }

        private boolean out(int operand) {
            output.accept((int) (getComboOperand(operand) % 8));
            return true;
        }

        private boolean bdv(int operand) {
            long comboOperand = getComboOperand(operand);
            B = (long) (A / Math.pow(2, comboOperand));
            return true;
        }

        private boolean cdv(int operand) {
            long comboOperand = getComboOperand(operand);
            C = (long) (A / Math.pow(2, comboOperand));
            return true;
        }

        private void checkValidProgram(List<Integer> program) {
            if (program.size() % 2 != 0) {
                throw new IllegalArgumentException();
            }
            for (int i = 0; i < program.size(); i = i + 2) {
                Integer opcode = program.get(i);
                if (opcode < 0 || opcode > 7) {
                    throw new IllegalArgumentException();
                }

                Integer operand = program.get(i + 1);
                if (operand < 0 || operand > 7) {
                    throw new IllegalArgumentException();
                }

                if (operand == 7 && comboOperandOpcodes.contains(opcode)) {
                    throw new IllegalArgumentException();
                }
            }
        }

        private long getComboOperand(int operand) {
            return switch (operand) {
                case 0 -> 0;
                case 1 -> 1;
                case 2 -> 2;
                case 3 -> 3;
                case 4 -> A;
                case 5 -> B;
                case 6 -> C;
                default -> throw new IllegalStateException();
            };
        }
    }
}
