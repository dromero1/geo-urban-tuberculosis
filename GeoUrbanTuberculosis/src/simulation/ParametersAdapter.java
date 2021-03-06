package simulation;

import java.util.HashMap;
import java.util.Map;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.parameter.Parameters;

public class ParametersAdapter {

	/**
	 * Exposed count parameter id
	 */
	private static final String EXPOSED_COUNT_PARAM_ID = "exposedCount";

	/**
	 * Susceptible count parameter id
	 */
	private static final String SUSCEPTIBLE_COUNT_PARAM_ID = "susceptibleCount";

	/**
	 * Alcohol drinkers share parameter id
	 */
	private static final String ALCOHOL_DRINKERS_SHARE_PARAM_ID = "alcoholDrinkersShare";

	/**
	 * Immunodepression share parameter id
	 */
	private static final String IMMUNODEPRESSION_SHARE_PARAM_ID = "immunodepressionShare";

	/**
	 * Smokers share parameter id
	 */
	private static final String SMOKERS_SHARE_PARAM_ID = "smokersShare";

	/**
	 * Average room volume parameter id
	 */
	private static final String AVERAGE_ROOM_VOLUME_PARAM_ID = "averageRoomVolume";

	/**
	 * Average room ventilation rate parameter id
	 */
	private static final String AVERAGE_ROOM_VENTILATION_RATE_PARAM_ID = "averageRoomVentilationRate";

	/**
	 * Mean diagnosis delay parameter id
	 */
	private static final String MEAN_DIAGNOSIS_DELAY_PARAM_ID = "meanDiagnosisDelay";

	/**
	 * Treatment dropout rate parameter id
	 */
	private static final String TREATMENT_DROPOUT_RATE_PARAM_ID = "treatmentDropoutRate";

	/**
	 * Infection probability parameter id
	 */
	private static final String INFECTION_PROBABILITY_PARAM_ID = "infectionProbability";

	/**
	 * Incidence rate goal parameter id
	 */
	private static final String INCIDENCE_RATE_GOAL_PARAM_ID = "incidenceRateGoal";

	/**
	 * Exposure rate goal parameter id
	 */
	private static final String EXPOSURE_RATE_GOAL_PARAM_ID = "exposureRateGoal";

	/**
	 * Epsilon parameter id
	 */
	private static final String EPSILON_PARAM_ID = "epsilon";

	/**
	 * Learning rate parameter id
	 */
	private static final String LEARNING_RATE_PARAM_ID = "learningRate";

	/**
	 * Discount factor parameter id
	 */
	private static final String DISCOUNT_FACTOR_PARAM_ID = "discountFactor";

	/**
	 * Tunable parameters
	 */
	private Map<String, Double> tunableParameters;

	/**
	 * Create a new parameters adapter
	 */
	public ParametersAdapter() {
		this.tunableParameters = new HashMap<>();
	}

	/**
	 * Initialize
	 */
	@ScheduledMethod(start = 0, priority = 4)
	public void init() {
		Parameters simParams = RunEnvironment.getInstance().getParameters();
		double averageRoomVentilationRate = simParams
				.getDouble(AVERAGE_ROOM_VENTILATION_RATE_PARAM_ID);
		setParameterValue(AVERAGE_ROOM_VENTILATION_RATE_PARAM_ID,
				averageRoomVentilationRate);
		double infectionProbability = simParams
				.getDouble(INFECTION_PROBABILITY_PARAM_ID);
		setParameterValue(INFECTION_PROBABILITY_PARAM_ID, infectionProbability);
	}

	/**
	 * Get exposed count
	 */
	public int getExposedCount() {
		Parameters simParams = RunEnvironment.getInstance().getParameters();
		return simParams.getInteger(EXPOSED_COUNT_PARAM_ID);
	}

	/**
	 * Get susceptible count
	 */
	public int getSusceptibleCount() {
		Parameters simParams = RunEnvironment.getInstance().getParameters();
		return simParams.getInteger(SUSCEPTIBLE_COUNT_PARAM_ID);
	}

	/**
	 * Get immunodepression share
	 */
	public double getImmunodepressionShare() {
		Parameters simParams = RunEnvironment.getInstance().getParameters();
		return simParams.getDouble(IMMUNODEPRESSION_SHARE_PARAM_ID);
	}

	/**
	 * Get alcohol drinkers share
	 */
	public double getAlcoholDrinkersShare() {
		Parameters simParams = RunEnvironment.getInstance().getParameters();
		return simParams.getDouble(ALCOHOL_DRINKERS_SHARE_PARAM_ID);
	}

	/**
	 * Get smokers share
	 */
	public double getSmokersShare() {
		Parameters simParams = RunEnvironment.getInstance().getParameters();
		return simParams.getDouble(SMOKERS_SHARE_PARAM_ID);
	}

	/**
	 * Get average room volume
	 */
	public double getAverageRoomVolume() {
		Parameters simParams = RunEnvironment.getInstance().getParameters();
		return simParams.getDouble(AVERAGE_ROOM_VOLUME_PARAM_ID);
	}

	/**
	 * Get incidence rate goal
	 */
	public double getIncidenceRateGoal() {
		Parameters simParams = RunEnvironment.getInstance().getParameters();
		return simParams.getDouble(INCIDENCE_RATE_GOAL_PARAM_ID);
	}

	/**
	 * Get exposure rate goal
	 */
	public double getExposureRateGoal() {
		Parameters simParams = RunEnvironment.getInstance().getParameters();
		return simParams.getDouble(EXPOSURE_RATE_GOAL_PARAM_ID);
	}

	/**
	 * Get mean diagnosis delay
	 */
	public double getMeanDiagnosisDelay() {
		Parameters simParams = RunEnvironment.getInstance().getParameters();
		return simParams.getDouble(MEAN_DIAGNOSIS_DELAY_PARAM_ID);
	}

	/**
	 * Get epsilon
	 */
	public double getEpsilon() {
		Parameters simParams = RunEnvironment.getInstance().getParameters();
		return simParams.getDouble(EPSILON_PARAM_ID);
	}

	/**
	 * Get learning rate
	 */
	public double getLearningRate() {
		Parameters simParams = RunEnvironment.getInstance().getParameters();
		return simParams.getDouble(LEARNING_RATE_PARAM_ID);
	}

	/**
	 * Get discount factor
	 */
	public double getDiscountFactor() {
		Parameters simParams = RunEnvironment.getInstance().getParameters();
		return simParams.getDouble(DISCOUNT_FACTOR_PARAM_ID);
	}

	/**
	 * Get treatment dropout rate
	 */
	public double getTreatmentDropoutRate() {
		Parameters simParams = RunEnvironment.getInstance().getParameters();
		return simParams.getDouble(TREATMENT_DROPOUT_RATE_PARAM_ID);
	}

	/**
	 * Get infection probability
	 */
	public double getInfectionProbability() {
		return this.tunableParameters.get(INFECTION_PROBABILITY_PARAM_ID);
	}

	/**
	 * Get average room ventilation rate
	 */
	public double getAverageRoomVentilationRate() {
		return this.tunableParameters
				.get(AVERAGE_ROOM_VENTILATION_RATE_PARAM_ID);
	}

	/**
	 * Get tunable parameters
	 */
	public Map<String, Double> getTunableParameters() {
		return this.tunableParameters;
	}

	/**
	 * Set parameter value
	 * 
	 * @param parameterId    Parameter id
	 * @param parameterValue Parameter value
	 */
	public void setParameterValue(String parameterId, double parameterValue) {
		this.tunableParameters.put(parameterId, parameterValue);
	}

}