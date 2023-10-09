package ru.job4j.hmap;

import java.util.*;

public class AnalyzeByMap {
    public static double averageScore(List<Pupil> pupils) {
        int subjQuantity = 0;
        int totalScore = 0;
        for (Pupil pupil : pupils) {
            for (Subject subject : pupil.subjects()) {
                totalScore += subject.score();
                subjQuantity++;
            }
        }
        return (double) totalScore / (subjQuantity == 0 ? 1 : subjQuantity);
    }

    public static List<Label> averageScoreByPupil(List<Pupil> pupils) {
        List<Label> rsl = new ArrayList<>();
        for (Pupil pupil : pupils) {
            int pupilsScore = 0;
            int pupilsSubjQty = 0;
            for (Subject subject : pupil.subjects()) {
                pupilsScore += subject.score();
                pupilsSubjQty++;
            }
            double pupilsAvgScore = (double) pupilsScore / (pupilsSubjQty == 0 ? 1 : pupilsSubjQty);
            rsl.add(new Label(pupil.name(), pupilsAvgScore));
        }
        return rsl;
    }

    public static List<Label> averageScoreBySubject(List<Pupil> pupils) {
        List<Label> rsl = new ArrayList<>();
        Map<String, Integer> subjectsMap = new LinkedHashMap<>();
        for (Pupil pupil : pupils) {
            for (Subject subject : pupil.subjects()) {
                int subjScore = subjectsMap.containsKey(subject.name())
                        ? subjectsMap.get(subject.name()) + subject.score()
                        : subject.score();
                subjectsMap.put(subject.name(), subjScore);
            }
        }
        for (String subjectName : subjectsMap.keySet()) {
            rsl.add(new Label(subjectName, (double) subjectsMap.get(subjectName) / pupils.size()));
        }
        return rsl;
    }

    public static Label bestStudent(List<Pupil> pupils) {
        List<Label> pupilsList = new LinkedList<>();
        for (Pupil pupil : pupils) {
            int pupilsScore = 0;
            for (Subject subject : pupil.subjects()) {
                pupilsScore += subject.score();
            }
            pupilsList.add(new Label(pupil.name(), pupilsScore));
        }
        pupilsList.sort(Comparator.naturalOrder());
        return pupilsList.get(pupilsList.size() - 1);
    }

    public static Label bestSubject(List<Pupil> pupils) {
        List<Label> subjList = new ArrayList<>();
        Map<String, Integer> subjectsMap = new LinkedHashMap<>();
        for (Pupil pupil : pupils) {
            for (Subject subject : pupil.subjects()) {
                int subjScore = subjectsMap.containsKey(subject.name())
                        ? subjectsMap.get(subject.name()) + subject.score()
                        : subject.score();
                subjectsMap.put(subject.name(), subjScore);
            }
        }
        for (String subjectName : subjectsMap.keySet()) {
            subjList.add(new Label(subjectName, (double) subjectsMap.get(subjectName)));
        }
        subjList.sort(Comparator.naturalOrder());
        return subjList.get(subjList.size() - 1);
    }
}