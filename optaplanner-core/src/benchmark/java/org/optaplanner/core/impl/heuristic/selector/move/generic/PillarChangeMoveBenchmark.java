package org.optaplanner.core.impl.heuristic.selector.move.generic;

import java.util.Arrays;
import java.util.Collections;

import org.openjdk.jmh.annotations.Benchmark;
import org.optaplanner.core.api.score.buildin.simple.SimpleScore;
import org.optaplanner.core.impl.domain.solution.descriptor.SolutionDescriptor;
import org.optaplanner.core.impl.domain.variable.descriptor.GenuineVariableDescriptor;
import org.optaplanner.core.impl.heuristic.move.AbstractPlannerMoveMicroBenchmark;
import org.optaplanner.core.impl.heuristic.move.Move;
import org.optaplanner.core.impl.score.director.easy.EasyScoreCalculator;
import org.optaplanner.core.impl.score.director.easy.EasyScoreDirector;
import org.optaplanner.core.impl.score.director.easy.EasyScoreDirectorFactory;
import org.optaplanner.core.impl.testdata.domain.TestdataValue;
import org.optaplanner.core.impl.testdata.domain.valuerange.entityproviding.TestdataEntityProvidingEntity;
import org.optaplanner.core.impl.testdata.domain.valuerange.entityproviding.TestdataEntityProvidingSolution;

public class PillarChangeMoveBenchmark extends AbstractPlannerMoveMicroBenchmark<TestdataEntityProvidingSolution> {

    private TestdataEntityProvidingEntity firstTestDataEntity;
    private TestdataEntityProvidingEntity secondTestDataEntity;
    private TestdataValue testdataValue;
    private SolutionDescriptor<TestdataEntityProvidingSolution> solutionDescriptor;

    @Override
    protected void initScoreDirector() {
        EasyScoreCalculator<TestdataEntityProvidingSolution> easyScoreCalculator = testdataSolution -> SimpleScore.of(0);

        EasyScoreDirector<TestdataEntityProvidingSolution> testdataSolutionEasyScoreDirector = new EasyScoreDirector<>
                (new EasyScoreDirectorFactory<>(solutionDescriptor,
                                                easyScoreCalculator),
                 true,
                 true,
                 easyScoreCalculator);
        TestdataEntityProvidingSolution workingSolution = new TestdataEntityProvidingSolution();
        workingSolution.setEntityList(Arrays.asList(firstTestDataEntity, secondTestDataEntity));
        testdataSolutionEasyScoreDirector.setWorkingSolution(workingSolution);
        super.setScoreDirector(testdataSolutionEasyScoreDirector);
    }

    @Override
    protected void initMove() {
        GenuineVariableDescriptor<TestdataEntityProvidingSolution> genuineVariableDescriptor =
                TestdataEntityProvidingEntity.buildVariableDescriptorForValue();
        solutionDescriptor = genuineVariableDescriptor.getEntityDescriptor().getSolutionDescriptor();

        super.setMove(new PillarChangeMove<>(Arrays.asList(firstTestDataEntity, secondTestDataEntity),
                                             genuineVariableDescriptor,
                                             testdataValue));
    }

    @Override
    protected void initEntities() {
        testdataValue = new TestdataValue("v1");
        firstTestDataEntity = new TestdataEntityProvidingEntity("e1", Collections.singletonList(testdataValue), null);
        secondTestDataEntity = new TestdataEntityProvidingEntity("e2", Collections.singletonList(testdataValue), null);
    }

    @Benchmark
    public Move<TestdataEntityProvidingSolution> benchmarkDoMove() {
        return super.benchmarkDoMove();
    }

    @Benchmark
    public Move<TestdataEntityProvidingSolution> benchmarkRebase() {
        return super.benchmarkRebase();
    }
}
