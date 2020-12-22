package model;

import java.util.List;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ISchedulableAction;
import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.query.space.grid.GridCell;
import repast.simphony.query.space.grid.GridCellNgh;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.grid.GridPoint;
import simulation.EventScheduler;
import simulation.SimulationBuilder;
import util.TickConverter;

public class Citizen {

	/**
	 * Particle expelling interval (unit: hours)
	 */
	public static final int PARTICLE_EXPELLING_INTERVAL = 1;

	/**
	 * Household
	 */
	private NdPoint household;

	/**
	 * Workplace
	 */
	private NdPoint workplace;

	/**
	 * Wake up time
	 */
	private double wakeUpTime;

	/**
	 * Returning home time
	 */
	private double returningHomeTime;

	/**
	 * Compartment
	 */
	private Compartment compartment;

	/**
	 * Is immunosuppressed? Whether the citizen is immunosuppressed or not.
	 */
	private boolean isImmunosuppressed;

	/**
	 * Smokes? Whether the citizen smokes or not.
	 */
	private boolean smokes;

	/**
	 * Drinks alcohol? Whether the citizen drinks alcohol or not.
	 */
	private boolean drinksAlcohol;

	/**
	 * Reference to simulation builder
	 */
	private SimulationBuilder simulationBuilder;

	/**
	 * Scheduled particle expelling action
	 */
	private ISchedulableAction expelAction;

	/**
	 * Create a new citizen agent
	 * 
	 * @param space       Continuous space
	 * @param grid        Grid
	 * @param compartment Compartment
	 */
	public Citizen(SimulationBuilder simulationBuilder,
			Compartment compartment) {
		this.simulationBuilder = simulationBuilder;
		this.compartment = compartment;
		this.wakeUpTime = Randomizer.getRandomWakeUpTime();
		this.returningHomeTime = Randomizer.getRandomReturningHomeTime();
		this.isImmunosuppressed = Randomizer.getRandomImmunodeficiency();
		this.smokes = Randomizer.getRandomSmoker();
		this.drinksAlcohol = Randomizer.getRandomAlcoholDrinker();
	}

	/**
	 * Initialize
	 */
	@ScheduledMethod(start = 0)
	public void init() {
		initDisease();
		scheduleRecurringEvents();
		goTo(this.household);
	}

	/**
	 * Expel particles
	 */
	public void expelParticles() {
		infect();
	}

	/**
	 * Wake up and go to workplace
	 */
	public void wakeUp() {
		goTo(this.workplace);
	}

	/**
	 * Return to household
	 */
	public void returnHome() {
		goTo(this.household);
	}

	/**
	 * Transition to the susceptible compartment
	 */
	public void transitionToSusceptible() {
		this.compartment = Compartment.SUSCEPTIBLE;
	}

	/**
	 * Transition to the exposed compartment
	 * 
	 * @param isInitialSetup Is initial setup?
	 */
	public void transitionToExposed(boolean isInitialSetup) {
		this.compartment = Compartment.EXPOSED;
		if (Randomizer.isGettingInfected(this) || isInitialSetup) {
			double incubationPeriod = Randomizer.getRandomIncubationPeriod();
			double ticks = TickConverter.daysToTicks(incubationPeriod);
			EventScheduler eventScheduler = EventScheduler.getInstance();
			eventScheduler.scheduleOneTimeEvent(ticks, this,
					"transitionToInfected");
		} else {
			transitionToSusceptible();
		}
	}

	/**
	 * Transition to the infected compartment
	 */
	public void transitionToInfected() {
		this.compartment = Compartment.INFECTED;
		// Schedule particle expelling
		EventScheduler eventScheduler = EventScheduler.getInstance();
		this.expelAction = eventScheduler.scheduleRecurringEvent(1, this,
				PARTICLE_EXPELLING_INTERVAL, "expelParticles");
		// Schedule diagnosis
		double daysToDiagnosis = Randomizer.getRandomDaysToDiagnosis();
		double ticks = TickConverter.daysToTicks(daysToDiagnosis);
		eventScheduler.scheduleOneTimeEvent(ticks, this,
				"transitionToOnTreament");
	}

	/**
	 * Transition to the on treatment compartment
	 */
	public void transitionToOnTreament() {
		this.compartment = Compartment.ON_TREATMENT;
		// Schedule treatment dropout or recovery
		if (Randomizer.isDroppingOutTreatment()) {
			transitionToInfected();
		} else {
			double treatmentDuration = Randomizer.getRandomTreatmentDuration();
			double ticks = TickConverter.daysToTicks(treatmentDuration);
			EventScheduler eventScheduler = EventScheduler.getInstance();
			eventScheduler.scheduleOneTimeEvent(ticks, this,
					"transitionToImmune");
		}
		// Unschedule particle expelling
		unscheduleAction(this.expelAction);
	}

	/**
	 * Transition to the immune compartment
	 */
	public void transitionToImmune() {
		this.compartment = Compartment.IMMUNE;
		// Schedule full recovery
		double daysToFullRecovery = Randomizer.getRandomDaysToFullRecovery();
		double ticks = TickConverter.daysToTicks(daysToFullRecovery);
		EventScheduler eventScheduler = EventScheduler.getInstance();
		eventScheduler.scheduleOneTimeEvent(ticks, this,
				"transitionToSusceptible");
	}

	/**
	 * Get compartment
	 */
	public Compartment getCompartment() {
		return this.compartment;
	}

	/**
	 * Get workplace location
	 */
	public NdPoint getWorkplaceLocation() {
		return this.workplace;
	}

	/**
	 * Set workplace location
	 * 
	 * @param workplaceLocation Workplace location
	 */
	public void setWorkplaceLocation(NdPoint workplaceLocation) {
		this.workplace = workplaceLocation;
	}

	/**
	 * Get household location
	 */
	public NdPoint getHouseholdLocation() {
		return this.household;
	}

	/**
	 * Set household location
	 * 
	 * @param householdLocation Household location
	 */
	public void setHouseholdLocation(NdPoint householdLocation) {
		this.household = householdLocation;
	}

	/**
	 * Smokes?
	 */
	public boolean smokes() {
		return this.smokes;
	}

	/**
	 * Drinks alcohol?
	 */
	public boolean drinksAlcohol() {
		return this.drinksAlcohol;
	}

	/**
	 * Is susceptible?
	 */
	public int isSusceptible() {
		return (this.compartment == Compartment.SUSCEPTIBLE) ? 1 : 0;
	}

	/**
	 * Is exposed?
	 */
	public int isExposed() {
		return (this.compartment == Compartment.EXPOSED) ? 1 : 0;
	}

	/**
	 * Is infected?
	 */
	public int isInfected() {
		return (this.compartment == Compartment.INFECTED) ? 1 : 0;
	}

	/**
	 * Is immune?
	 */
	public int isImmune() {
		return (this.compartment == Compartment.IMMUNE) ? 1 : 0;
	}

	/**
	 * Is on treatment?
	 */
	public int isOnTreatment() {
		return (this.compartment == Compartment.ON_TREATMENT) ? 1 : 0;
	}

	/**
	 * Is an active case?
	 */
	public int isActiveCase() {
		return (this.compartment == Compartment.EXPOSED
				|| this.compartment == Compartment.INFECTED
				|| this.compartment == Compartment.ON_TREATMENT) ? 1 : 0;
	}

	/**
	 * Is immunodepressed?
	 */
	public boolean isImmunodepressed() {
		return this.isImmunosuppressed;
	}

	/**
	 * Infect nearby susceptible individuals
	 */
	private void infect() {
		GridPoint pt = this.simulationBuilder.grid.getLocation(this);
		GridCellNgh<Citizen> nghCreator = new GridCellNgh<>(
				this.simulationBuilder.grid, pt, Citizen.class, 0, 0);
		List<GridCell<Citizen>> gridCells = nghCreator.getNeighborhood(true);
		for (GridCell<Citizen> cell : gridCells) {
			int infectedCount = countInfectedPeople(cell.items());
			for (Citizen citizen : cell.items()) {
				if (citizen.compartment == Compartment.SUSCEPTIBLE
						&& Randomizer.isGettingExposed(infectedCount)) {
					citizen.transitionToExposed(false);
				}
			}
		}
	}

	/**
	 * Initialize disease
	 */
	private void initDisease() {
		if (this.compartment == Compartment.EXPOSED) {
			transitionToExposed(true);
		}
	}

	/**
	 * Schedule recurring events
	 */
	private void scheduleRecurringEvents() {
		EventScheduler eventScheduler = EventScheduler.getInstance();
		eventScheduler.scheduleRecurringEvent(this.wakeUpTime, this,
				TickConverter.TICKS_PER_DAY, "wakeUp");
		eventScheduler.scheduleRecurringEvent(this.returningHomeTime, this,
				TickConverter.TICKS_PER_DAY, "returnHome");
	}

	/**
	 * Count infected people
	 * 
	 * @param citizens Citizens
	 */
	private int countInfectedPeople(Iterable<Citizen> citizens) {
		int count = 0;
		for (Citizen citizen : citizens) {
			if (citizen.compartment == Compartment.INFECTED) {
				count++;
			}
		}
		return count;
	}

	/**
	 * Go to location
	 * 
	 * @param location Location
	 */
	private void goTo(NdPoint location) {
		double x = location.getX();
		double y = location.getY();
		this.simulationBuilder.space.moveTo(this, x, y);
		this.simulationBuilder.grid.moveTo(this, (int) x, (int) y);
	}

	/**
	 * Unschedule action
	 * 
	 * @param action Action to unschedule
	 */
	private void unscheduleAction(ISchedulableAction action) {
		ISchedule schedule = RunEnvironment.getInstance().getCurrentSchedule();
		schedule.removeAction(action);
	}

}