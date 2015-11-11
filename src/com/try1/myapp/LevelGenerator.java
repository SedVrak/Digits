package com.try1.myapp;

import com.try1.myapp.scenes.LevelType;

import java.util.Random;

/**
 * Created by SeniorJD.
 */
public abstract class LevelGenerator {
    public class Task {
        String task;
        String expectedResult;

        public Task(String task, String expectedResult) {
            this.task = task;
            this.expectedResult = expectedResult;
        }

        public String getTask() {
            return task;
        }

        public String getExpectedResult() {
            return expectedResult;
        }
    }

    public abstract Task generateTask();
    public Task generateTask(int score) {
        return generateTask();
    }

    public static LevelGenerator getGenerator(GameType gameType, LevelType levelType, int levelIndex) {
        switch (gameType) {
            case TIME_ATTACK:
                return getTimeAttackGenerator();
            case LEVELS:
                return getLevelsGenerator(levelType, levelIndex);
        }
        return null;
    }

    static LevelGenerator getTimeAttackGenerator() {
        return new TimeAttackGenerator();
    }

    static LevelGenerator getLevelsGenerator(LevelType levelType, int levelIndex) {
        switch (levelType) {
            case PLUS:
                return getLevelsPlusGenerator(levelIndex);
            case MINUS:
                return getLevelsMinusGenerator(levelIndex);
            case MULTIPLY:
                return getLevelsMultiplyGenerator(levelIndex);
//            case DIVIDE:
//                return getLevelsDivideGenerator(levelIndex);
        }

        return null;
    }

    static LevelGenerator getLevelsPlusGenerator(int levelIndex) {
        switch (levelIndex) {
            case 0:
                return new LevelsPlus0Generator();
            case 1:
                return new LevelsPlus1Generator();
            case 2:
                return new LevelsPlus2Generator();
            case 3:
                return new LevelsPlus3Generator();
            case 4:
                return new LevelsPlus4Generator();
            case 5:
                return new LevelsPlus5Generator();
            case 6:
                return new LevelsPlus6Generator();
            case 7:
                return new LevelsPlus7Generator();
            case 8:
                return new LevelsPlus8Generator();
            case 9:
                return new LevelsPlus9Generator();
            case 10:
                return new LevelsPlus10Generator();
            case 11:
                return new LevelsPlus11Generator();
            case 12:
                return new LevelsPlus12Generator();
            case 13:
                return new LevelsPlus13Generator();
            case 14:
                return new LevelsPlus14Generator();
        }

        return null;
    }

    static LevelGenerator getLevelsMinusGenerator(int levelIndex) {
        switch (levelIndex) {
            case 0:
                return new LevelsMinus0Generator();
            case 1:
                return new LevelsMinus1Generator();
            case 2:
                return new LevelsMinus2Generator();
            case 3:
                return new LevelsMinus3Generator();
            case 4:
                return new LevelsMinus4Generator();
            case 5:
                return new LevelsMinus5Generator();
            case 6:
                return new LevelsMinus6Generator();
            case 7:
                return new LevelsMinus7Generator();
            case 8:
                return new LevelsMinus8Generator();
            case 9:
                return new LevelsMinus9Generator();
            case 10:
                return new LevelsMinus10Generator();
            case 11:
                return new LevelsMinus11Generator();
            case 12:
                return new LevelsMinus12Generator();
            case 13:
                return new LevelsMinus13Generator();
            case 14:
                return new LevelsMinus14Generator();
        }
        return null;
    }

    static LevelGenerator getLevelsMultiplyGenerator(int levelIndex) {
        switch (levelIndex) {
            case 0:
                return new LevelsMultiply0Generator();
            case 1:
                return new LevelsMultiply1Generator();
            case 2:
                return new LevelsMultiply2Generator();
            case 3:
                return new LevelsMultiply3Generator();
            case 4:
                return new LevelsMultiply4Generator();
            case 5:
                return new LevelsMultiply5Generator();
            case 6:
                return new LevelsMultiply6Generator();
            case 7:
                return new LevelsMultiply7Generator();
            case 8:
                return new LevelsMultiply8Generator();
            case 9:
                return new LevelsMultiply9Generator();
            case 10:
                return new LevelsMultiply10Generator();
            case 11:
                return new LevelsMultiply11Generator();
            case 12:
                return new LevelsMultiply12Generator();
            case 13:
                return new LevelsMultiply13Generator();
            case 14:
                return new LevelsMultiply14Generator();
        }
        return null;
    }

    static LevelGenerator getLevelsDivideGenerator(int levelIndex) {
        switch (levelIndex) {

        }
        return null;
    }

    // GENERATOR CLASSES
    static class TimeAttackGenerator extends LevelGenerator {

        @Override
        public Task generateTask(int score) {
            int levelTypeIndex = (score / 10) / 15;
            int levelDifficultyIndex = (score / 10) % 15;

            if (levelTypeIndex > 2) {
                levelTypeIndex = 2;
                levelDifficultyIndex = 14;
            }

            for (LevelType levelType : LevelType.values()) {
                if (levelType.getIndex() == levelTypeIndex) {
                    return getLevelsGenerator(levelType, levelDifficultyIndex).generateTask();
                }
            }

            return generateTask();
        }

        @Override
        public Task generateTask() {
            Random r = new Random();
            int i1 = r.nextInt(100);
            int i2 = r.nextInt(100);

            String task;
            int result;

            if (r.nextBoolean()) {
                result = i1 + i2;
                task = i1 + "+" +i2;
            } else {
                result = i1 - i2;
                task = i1 + "-" +i2;
            }

            return new Task(task, String.valueOf(result));
        }
    }

    static class LevelsPlus0Generator extends LevelGenerator {

        @Override
        public Task generateTask() {
            Random r = new Random();
            int i1 = r.nextInt(10);
            int i2 = r.nextInt(10);

            String task;
            int result;

            result = i1 + i2;
            task = i1 + "+" +i2;

            return new Task(task, String.valueOf(result));
        }
    }

    static class LevelsPlus1Generator extends LevelGenerator {

        @Override
        public Task generateTask() {
            Random r = new Random();
            int i1 = r.nextInt(10) + 10;
            int i2 = r.nextInt(10) + 10;

            String task;
            int result;

            result = i1 + i2;
            task = i1 + "+" +i2;

            return new Task(task, String.valueOf(result));
        }
    }

    static class LevelsPlus2Generator extends LevelGenerator {

        @Override
        public Task generateTask() {
            Random r = new Random();
            int i1 = r.nextInt(20) + 10;
            int i2 = r.nextInt(9) + 1;

            String task;
            int result;

            result = i1 + i2;
            task = i1 + "+" +i2;

            return new Task(task, String.valueOf(result));
        }
    }

    static class LevelsPlus3Generator extends LevelGenerator {

        @Override
        public Task generateTask() {
            Random r = new Random();
            int i1 = r.nextInt(50) + 10;
            int i2 = r.nextInt(20) + 10;

            String task;
            int result;

            result = i1 + i2;
            task = i1 + "+" +i2;

            return new Task(task, String.valueOf(result));
        }
    }

    static class LevelsPlus4Generator extends LevelGenerator {

        @Override
        public Task generateTask() {
            Random r = new Random();
            int i1 = r.nextInt(50) + 10;
            int i2 = r.nextInt(50) + 10;

            String task;
            int result;

            result = i1 + i2;
            task = i1 + "+" +i2;

            return new Task(task, String.valueOf(result));
        }
    }

    static class LevelsPlus5Generator extends LevelGenerator {

        @Override
        public Task generateTask() {
            Random r = new Random();
            int i1 = r.nextInt(60) + 10;
            int i2 = r.nextInt(70);

            String task;
            int result;

            result = i1 + i2;
            task = i1 + "+" +i2;

            return new Task(task, String.valueOf(result));
        }
    }

    static class LevelsPlus6Generator extends LevelGenerator {

        @Override
        public Task generateTask() {
            Random r = new Random();
            int i1 = r.nextInt(100);
            int i2 = r.nextInt(100);

            String task;
            int result;

            result = i1 + i2;
            task = i1 + "+" +i2;

            return new Task(task, String.valueOf(result));
        }
    }

    static class LevelsPlus7Generator extends LevelGenerator {

        @Override
        public Task generateTask() {
            Random r = new Random();
            int i1 = r.nextInt(20);
            int i2 = r.nextInt(20);
            int i3 = r.nextInt(20);

            String task;
            int result;

            result = i1 + i2 + i3;
            task = i1 + "+" +i2 + "+" +i3;

            return new Task(task, String.valueOf(result));
        }
    }

    static class LevelsPlus8Generator extends LevelGenerator {

        @Override
        public Task generateTask() {
            Random r = new Random();
            int i1 = r.nextInt(40);
            int i2 = r.nextInt(40);
            int i3 = r.nextInt(19) + 1;

            String task;
            int result;

            result = i1 + i2 + i3;
            task = i1 + "+" +i2 + "+" +i3;

            return new Task(task, String.valueOf(result));
        }
    }

    static class LevelsPlus9Generator extends LevelGenerator {

        @Override
        public Task generateTask() {
            Random r = new Random();
            int i1 = r.nextInt(60);
            int i2 = r.nextInt(19) + 1;
            int i3 = r.nextInt(60);

            String task;
            int result;

            result = i1 + i2 + i3;
            task = i1 + "+" +i2 + "+" +i3;

            return new Task(task, String.valueOf(result));
        }
    }

    static class LevelsPlus10Generator extends LevelGenerator {

        @Override
        public Task generateTask() {
            Random r = new Random();
            int i1 = r.nextInt(100);
            int i2 = r.nextInt(19) + 1;
            int i3 = r.nextInt(100);

            String task;
            int result;

            result = i1 + i2 + i3;
            task = i1 + "+" +i2 + "+" +i3;

            return new Task(task, String.valueOf(result));
        }
    }

    static class LevelsPlus11Generator extends LevelGenerator {

        @Override
        public Task generateTask() {
            Random r = new Random();
            int i1 = r.nextInt(100);
            int i2 = r.nextInt(100);
            int i3 = r.nextInt(100);

            String task;
            int result;

            result = i1 + i2 + i3;
            task = i1 + "+" +i2 + "+" +i3;

            return new Task(task, String.valueOf(result));
        }
    }

    static class LevelsPlus12Generator extends LevelGenerator {

        @Override
        public Task generateTask() {
            Random r = new Random();
            int i1 = r.nextInt(100) + 100;
            int i2 = r.nextInt(100);
            int i3 = r.nextInt(100);

            String task;
            int result;

            result = i1 + i2 + i3;
            task = i1 + "+" +i2 + "+" +i3;

            return new Task(task, String.valueOf(result));
        }
    }

    static class LevelsPlus13Generator extends LevelGenerator {

        @Override
        public Task generateTask() {
            Random r = new Random();
            int i1 = r.nextInt(100) + 100;
            int i2 = r.nextInt(100) + 50;
            int i3 = r.nextInt(100) + 50;

            String task;
            int result;

            result = i1 + i2 + i3;
            task = i1 + "+" +i2 + "+" +i3;

            return new Task(task, String.valueOf(result));
        }
    }

    static class LevelsPlus14Generator extends LevelGenerator {

        @Override
        public Task generateTask() {
            Random r = new Random();
            int i1 = r.nextInt(100) + 100;
            int i2 = r.nextInt(100) + 100;
            int i3 = r.nextInt(100) + 100;

            String task;
            int result;

            result = i1 + i2 + i3;
            task = i1 + "+" +i2 + "+" +i3;

            return new Task(task, String.valueOf(result));
        }
    }

    static class LevelsMinus0Generator extends LevelGenerator {

        @Override
        public Task generateTask() {
            Random r = new Random();
            int test1 = r.nextInt(10);
            int test2 = r.nextInt(10);

            int i1 = Math.max(test1, test2);
            int i2 = Math.min(test1, test2);

            String task;
            int result;

            if (r.nextBoolean()) {
                result = i1 + i2;
                task = i1 + "+" +i2;
            } else {
                result = i1 - i2;
                task = i1 + "-" +i2;
            }

            return new Task(task, String.valueOf(result));
        }
    }

    static class LevelsMinus1Generator extends LevelGenerator {

        @Override
        public Task generateTask() {
            Random r = new Random();
            int test1 = r.nextInt(10) + 10;
            int test2 = r.nextInt(10) + 10;

            int i1 = Math.max(test1, test2);
            int i2 = Math.min(test1, test2);

            String task;
            int result;

            if (r.nextBoolean()) {
                result = i1 + i2;
                task = i1 + "+" +i2;
            } else {
                result = i1 - i2;
                task = i1 + "-" +i2;
            }

            return new Task(task, String.valueOf(result));
        }
    }

    static class LevelsMinus2Generator extends LevelGenerator {

        @Override
        public Task generateTask() {
            Random r = new Random();
            int test1 = r.nextInt(30) + 10;
            int test2 = r.nextInt(9) + 1;

            int i1 = Math.max(test1, test2);
            int i2 = Math.min(test1, test2);

            String task;
            int result;

            if (r.nextBoolean()) {
                result = i1 + i2;
                task = i1 + "+" +i2;
            } else {
                result = i1 - i2;
                task = i1 + "-" +i2;
            }

            return new Task(task, String.valueOf(result));
        }
    }

    static class LevelsMinus3Generator extends LevelGenerator {

        @Override
        public Task generateTask() {
            Random r = new Random();
            int test1 = r.nextInt(50) + 10;
            int test2 = r.nextInt(19) + 1;

            int i1 = Math.max(test1, test2);
            int i2 = Math.min(test1, test2);

            String task;
            int result;

            if (r.nextBoolean()) {
                result = i1 + i2;
                task = i1 + "+" +i2;
            } else {
                result = i1 - i2;
                task = i1 + "-" +i2;
            }

            return new Task(task, String.valueOf(result));
        }
    }

    static class LevelsMinus4Generator extends LevelGenerator {

        @Override
        public Task generateTask() {
            Random r = new Random();
            int i1 = r.nextInt(50) + 10;
            int i2 = r.nextInt(20) + 10;

            String task;
            int result;

            if (r.nextBoolean()) {
                result = i1 + i2;
                task = i1 + "+" +i2;
            } else {
                result = i1 - i2;
                task = i1 + "-" +i2;
            }

            return new Task(task, String.valueOf(result));
        }
    }

    static class LevelsMinus5Generator extends LevelGenerator {

        @Override
        public Task generateTask() {
            Random r = new Random();
            int i1 = r.nextInt(70) + 10;
            int i2 = r.nextInt(20) + 10;

            String task;
            int result;

            if (r.nextBoolean()) {
                result = i1 + i2;
                task = i1 + "+" +i2;
            } else {
                result = i1 - i2;
                task = i1 + "-" +i2;
            }

            return new Task(task, String.valueOf(result));
        }
    }

    static class LevelsMinus6Generator extends LevelGenerator {

        @Override
        public Task generateTask() {
            Random r = new Random();
            int i1 = r.nextInt(100) + 1;
            int i2 = r.nextInt(30) + 10;

            String task;
            int result;

            if (r.nextBoolean()) {
                result = i1 + i2;
                task = i1 + "+" +i2;
            } else {
                result = i1 - i2;
                task = i1 + "-" +i2;
            }

            return new Task(task, String.valueOf(result));
        }
    }

    static class LevelsMinus7Generator extends LevelGenerator {

        @Override
        public Task generateTask() {
            Random r = new Random();
            int i1 = r.nextInt(100) + 1;
            int i2 = r.nextInt(50) + 1;

            String task;
            int result;

            if (r.nextBoolean()) {
                result = i1 + i2;
                task = i1 + "+" +i2;
            } else {
                result = i1 - i2;
                task = i1 + "-" +i2;
            }

            return new Task(task, String.valueOf(result));
        }
    }

    static class LevelsMinus8Generator extends LevelGenerator {

        @Override
        public Task generateTask() {
            Random r = new Random();
            int i1 = r.nextInt(100) + 1;
            int i2 = r.nextInt(100) + 1;

            String task;
            int result;

            if (r.nextBoolean()) {
                result = i1 + i2;
                task = i1 + "+" +i2;
            } else {
                result = i1 - i2;
                task = i1 + "-" +i2;
            }

            return new Task(task, String.valueOf(result));
        }
    }

    static class LevelsMinus9Generator extends LevelGenerator {

        @Override
        public Task generateTask() {
            Random r = new Random();
            int i1 = r.nextInt(20) + 1;
            int i2 = r.nextInt(20) + 1;
            int i3 = r.nextInt(20) + 1;

            String task;
            int result;

            if (r.nextBoolean()) {
                result = i1 + i2;
                task = i1 + "+" +i2;
                if (r.nextBoolean()) {
                    result = result + i3;
                    task = task + "+" + i3;
                } else {
                    result = result - i3;
                    task = task + "-" + i3;
                }
            } else {
                result = i1 - i2;
                task = i1 + "-" +i2;

                if (r.nextBoolean()) {
                    result = result + i3;
                    task = task + "+" + i3;
                } else {
                    result = result - i3;
                    task = task + "-" + i3;
                }
            }

            return new Task(task, String.valueOf(result));
        }
    }

    static class LevelsMinus10Generator extends LevelGenerator {

        @Override
        public Task generateTask() {
            Random r = new Random();
            int i1 = r.nextInt(40) + 1;
            int i2 = r.nextInt(20) + 1;
            int i3 = r.nextInt(40) + 1;

            String task;
            int result;

            if (r.nextBoolean()) {
                result = i1 + i2;
                task = i1 + "+" +i2;
                if (r.nextBoolean()) {
                    result = result + i3;
                    task = task + "+" + i3;
                } else {
                    result = result - i3;
                    task = task + "-" + i3;
                }
            } else {
                result = i1 - i2;
                task = i1 + "-" +i2;

                if (r.nextBoolean()) {
                    result = result + i3;
                    task = task + "+" + i3;
                } else {
                    result = result - i3;
                    task = task + "-" + i3;
                }
            }

            return new Task(task, String.valueOf(result));
        }
    }

    static class LevelsMinus11Generator extends LevelGenerator {

        @Override
        public Task generateTask() {
            Random r = new Random();
            int i1 = r.nextInt(60) + 1;
            int i2 = r.nextInt(40) + 1;
            int i3 = r.nextInt(60) + 1;

            String task;
            int result;

            if (r.nextBoolean()) {
                result = i1 + i2;
                task = i1 + "+" +i2;
                if (r.nextBoolean()) {
                    result = result + i3;
                    task = task + "+" + i3;
                } else {
                    result = result - i3;
                    task = task + "-" + i3;
                }
            } else {
                result = i1 - i2;
                task = i1 + "-" +i2;

                if (r.nextBoolean()) {
                    result = result + i3;
                    task = task + "+" + i3;
                } else {
                    result = result - i3;
                    task = task + "-" + i3;
                }
            }

            return new Task(task, String.valueOf(result));
        }
    }

    static class LevelsMinus12Generator extends LevelGenerator {

        @Override
        public Task generateTask() {
            Random r = new Random();
            int i1 = r.nextInt(80) + 1;
            int i2 = r.nextInt(50) + 1;
            int i3 = r.nextInt(80) + 1;

            String task;
            int result;

            if (r.nextBoolean()) {
                result = i1 + i2;
                task = i1 + "+" +i2;
                if (r.nextBoolean()) {
                    result = result + i3;
                    task = task + "+" + i3;
                } else {
                    result = result - i3;
                    task = task + "-" + i3;
                }
            } else {
                result = i1 - i2;
                task = i1 + "-" +i2;

                if (r.nextBoolean()) {
                    result = result + i3;
                    task = task + "+" + i3;
                } else {
                    result = result - i3;
                    task = task + "-" + i3;
                }
            }

            return new Task(task, String.valueOf(result));
        }
    }

    static class LevelsMinus13Generator extends LevelGenerator {

        @Override
        public Task generateTask() {
            Random r = new Random();
            int i1 = r.nextInt(100) + 50;
            int i2 = r.nextInt(75) + 1;
            int i3 = r.nextInt(100) + 1;

            String task;
            int result;

            if (r.nextBoolean()) {
                result = i1 + i2;
                task = i1 + "+" +i2;
                if (r.nextBoolean()) {
                    result = result + i3;
                    task = task + "+" + i3;
                } else {
                    result = result - i3;
                    task = task + "-" + i3;
                }
            } else {
                result = i1 - i2;
                task = i1 + "-" +i2;

                if (r.nextBoolean()) {
                    result = result + i3;
                    task = task + "+" + i3;
                } else {
                    result = result - i3;
                    task = task + "-" + i3;
                }
            }

            return new Task(task, String.valueOf(result));
        }
    }

    static class LevelsMinus14Generator extends LevelGenerator {

        @Override
        public Task generateTask() {
            Random r = new Random();
            int i1 = r.nextInt(100) + 100;
            int i2 = r.nextInt(100) + 1;
            int i3 = r.nextInt(100) + 1;

            String task;
            int result;

            if (r.nextBoolean()) {
                result = i1 + i2;
                task = i1 + "+" +i2;
                if (r.nextBoolean()) {
                    result = result + i3;
                    task = task + "+" + i3;
                } else {
                    result = result - i3;
                    task = task + "-" + i3;
                }
            } else {
                result = i1 - i2;
                task = i1 + "-" +i2;

                if (r.nextBoolean()) {
                    result = result + i3;
                    task = task + "+" + i3;
                } else {
                    result = result - i3;
                    task = task + "-" + i3;
                }
            }

            return new Task(task, String.valueOf(result));
        }
    }

    static class LevelsMultiply0Generator extends LevelGenerator {

        @Override
        public Task generateTask() {
            Random r = new Random();
            int i1 = r.nextInt(5);
            int i2 = r.nextInt(5);

            String task;
            int result;

            result = i1 * i2;
            task = i1 + "x" +i2;

            return new Task(task, String.valueOf(result));
        }
    }

    static class LevelsMultiply1Generator extends LevelGenerator {

        @Override
        public Task generateTask() {
            Random r = new Random();
            int i1 = r.nextInt(5) + 5;
            int i2 = r.nextInt(5) + 5;

            String task;
            int result;

            result = i1 * i2;
            task = i1 + "x" +i2;

            return new Task(task, String.valueOf(result));
        }
    }

    static class LevelsMultiply2Generator extends LevelGenerator {

        @Override
        public Task generateTask() {
            Random r = new Random();
            int i1 = r.nextInt(10) + 11;
            int i2 = r.nextInt(10) + 1;

            String task;
            int result;

            result = i1 * i2;
            task = i1 + "x" +i2;

            return new Task(task, String.valueOf(result));
        }
    }

    static class LevelsMultiply3Generator extends LevelGenerator {

        @Override
        public Task generateTask() {
            Random r = new Random();
            int i1 = r.nextInt(10) + 21;
            int i2 = r.nextInt(10) + 1;

            String task;
            int result;

            result = i1 * i2;
            task = i1 + "x" +i2;

            return new Task(task, String.valueOf(result));
        }
    }

    static class LevelsMultiply4Generator extends LevelGenerator {

        @Override
        public Task generateTask() {
            Random r = new Random();
            int i1 = r.nextInt(30) + 11;
            int i2 = r.nextInt(10) + 1;

            String task;
            int result;

            result = i1 * i2;
            task = i1 + "x" +i2;

            return new Task(task, String.valueOf(result));
        }
    }

    static class LevelsMultiply5Generator extends LevelGenerator {

        @Override
        public Task generateTask() {
            Random r = new Random();
            int i1 = r.nextInt(30) + 11;
            int i2 = r.nextInt(20) + 1;

            String task;
            int result;

            result = i1 * i2;
            task = i1 + "x" +i2;

            return new Task(task, String.valueOf(result));
        }
    }

    static class LevelsMultiply6Generator extends LevelGenerator {

        @Override
        public Task generateTask() {
            Random r = new Random();
            int i1 = r.nextInt(40) + 11;
            int i2 = r.nextInt(30) + 1;

            String task;
            int result;

            result = i1 * i2;
            task = i1 + "x" +i2;

            return new Task(task, String.valueOf(result));
        }
    }

    static class LevelsMultiply7Generator extends LevelGenerator {

        @Override
        public Task generateTask() {
            Random r = new Random();
            int i1 = r.nextInt(50) + 11;
            int i2 = r.nextInt(50) + 1;

            String task;
            int result;

            result = i1 * i2;
            task = i1 + "x" +i2;

            return new Task(task, String.valueOf(result));
        }
    }

    static class LevelsMultiply8Generator extends LevelGenerator {

        @Override
        public Task generateTask() {
            Random r = new Random();
            int i1 = r.nextInt(60) + 11;
            int i2 = r.nextInt(50) + 1;

            String task;
            int result;

            result = i1 * i2;
            task = i1 + "x" +i2;

            return new Task(task, String.valueOf(result));
        }
    }

    static class LevelsMultiply9Generator extends LevelGenerator {

        @Override
        public Task generateTask() {
            Random r = new Random();
            int i1 = r.nextInt(20) + 11;
            int i2 = r.nextInt(9) + 1;
            int i3 = r.nextInt(20) + 11;

            String task;
            int result;

            if (r.nextBoolean()) {
                if ((i1 + i2 + i3) % 2 == 0) {
                    result = i1 * i2 + i3;
                    task = i1 + "*" +i2 + "+" +i3;
                } else {
                    result = i1 + i2 * i3;
                    task = i1 + "+" +i2 + "*" +i3;
                }

            } else {
                if ((i1 + i2 + i3) % 2 == 0) {
                    result = i1 * i2 - i3;
                    task = i1 + "*" +i2 + "-" +i3;
                } else {
                    result = i1 - i2 * i3;
                    task = i1 + "-" +i2 + "*" +i3;
                }
            }

            return new Task(task, String.valueOf(result));
        }
    }

    static class LevelsMultiply10Generator extends LevelGenerator {

        @Override
        public Task generateTask() {
            Random r = new Random();
            int i1 = r.nextInt(50) + 11;
            int i2 = r.nextInt(9) + 1;
            int i3 = r.nextInt(50) + 11;

            String task;
            int result;

            if (r.nextBoolean()) {
                if ((i1 + i2 + i3) % 2 == 0) {
                    result = i1 * i2 + i3;
                    task = i1 + "*" +i2 + "+" +i3;
                } else {
                    result = i1 + i2 * i3;
                    task = i1 + "+" +i2 + "*" +i3;
                }

            } else {
                if ((i1 + i2 + i3) % 2 == 0) {
                    result = i1 * i2 - i3;
                    task = i1 + "*" +i2 + "-" +i3;
                } else {
                    result = i1 - i2 * i3;
                    task = i1 + "-" +i2 + "*" +i3;
                }
            }

            return new Task(task, String.valueOf(result));
        }
    }

    static class LevelsMultiply11Generator extends LevelGenerator {

        @Override
        public Task generateTask() {
            Random r = new Random();
            int i1 = r.nextInt(75) + 1;
            int i2 = r.nextInt(9) + 1;
            int i3 = r.nextInt(75) + 1;

            String task;
            int result;

            if (r.nextBoolean()) {
                if ((i1 + i2 + i3) % 2 == 0) {
                    result = i1 * i2 + i3;
                    task = i1 + "*" +i2 + "+" +i3;
                } else {
                    result = i1 + i2 * i3;
                    task = i1 + "+" +i2 + "*" +i3;
                }

            } else {
                if ((i1 + i2 + i3) % 2 == 0) {
                    result = i1 * i2 - i3;
                    task = i1 + "*" +i2 + "-" +i3;
                } else {
                    result = i1 - i2 * i3;
                    task = i1 + "-" +i2 + "*" +i3;
                }
            }

            return new Task(task, String.valueOf(result));
        }
    }

    static class LevelsMultiply12Generator extends LevelGenerator {

        @Override
        public Task generateTask() {
            Random r = new Random();
            int i1 = r.nextInt(10) + 1;
            int i2 = r.nextInt(10) + 1;
            int i3 = r.nextInt(10) + 1;

            String task;
            int result;

            result = i1 * i2 * i3;
            task = i1 + "*" +i2 + "*" +i3;

            return new Task(task, String.valueOf(result));
        }
    }

    static class LevelsMultiply13Generator extends LevelGenerator {

        @Override
        public Task generateTask() {
            Random r = new Random();
            int i1 = r.nextInt(10) + 11;
            int i2 = r.nextInt(10) + 11;
            int i3 = r.nextInt(10) + 11;

            String task;
            int result;

            result = i1 * i2 * i3;
            task = i1 + "*" +i2 + "*" +i3;

            return new Task(task, String.valueOf(result));
        }
    }

    static class LevelsMultiply14Generator extends LevelGenerator {

        @Override
        public Task generateTask() {
            Random r = new Random();
            int i1 = r.nextInt(20) + 11;
            int i2 = r.nextInt(20) + 11;
            int i3 = r.nextInt(20) + 11;

            String task;
            int result;

            result = i1 * i2 * i3;
            task = i1 + "*" +i2 + "*" +i3;

            return new Task(task, String.valueOf(result));
        }
    }
}
