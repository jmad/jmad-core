/**
 * Copyright (c) 2018 European Organisation for Nuclear Research (CERN), All Rights Reserved.
 */

package cern.accsoft.steering.jmad.tools.modeldefs.creating.lang;

import static java.util.Objects.requireNonNull;

import cern.accsoft.steering.jmad.domain.beam.Beam;
import cern.accsoft.steering.jmad.domain.beam.Beam.Direction;
import cern.accsoft.steering.jmad.domain.beam.Beam.Particle;

public class OngoingBeam {

    private final Beam beam;

    public OngoingBeam(Beam beam) {
        this.beam = requireNonNull(beam, "beam must not be null");
    }

    public void particle(Particle particle) {
        beam.setParticle(particle);
    }

    public void particle(String particleName) {
        beam.setParticleName(particleName);
    }

    public void mass(Double mass) {
        beam.setMass(mass);
    }

    public void charge(Double charge) {
        beam.setCharge(charge);
    }

    public void energy(Double energy) {
        beam.setEnergy(energy);
    }

    public void pc(Double momentum) {
        beam.setMomentum(momentum);
    }

    public void gamma(Double gamma) {
        beam.setGamma(gamma);
    }

    public void ex(Double horizontalEmittance) {
        beam.setHorizontalEmittance(horizontalEmittance);
    }

    public void ey(Double verticalEmittance) {
        beam.setVerticalEmittance(verticalEmittance);
    }

    public void et(Double longitudinalEmittance) {
        beam.setLongitudinalEmittance(longitudinalEmittance);
    }

    public void exn(Double normalisedHorizontalEmittance) {
        beam.setNormalisedHorizontalEmittance(normalisedHorizontalEmittance);
    }

    public void eyn(Double normalisedVerticalEmittance) {
        beam.setNormalisedVerticalEmittance(normalisedVerticalEmittance);
    }

    public void sigt(Double bunchLength) {
        beam.setBunchLength(bunchLength);
    }

    public void sige(Double relativeEnergySpread) {
        beam.setRelativeEnergySpread(relativeEnergySpread);
    }

    public void kbunch(Integer bunchNumber) {
        beam.setBunchNumber(bunchNumber);
    }

    public void npart(Double particleNumber) {
        beam.setParticleNumber(particleNumber);
    }

    public void bcurrent(Double bunchCurrent) {
        beam.setBunchCurrent(bunchCurrent);
    }

    public void bunched(Boolean bunched) {
        beam.setBunched(bunched);
    }

    public void radiate(Boolean radiate) {
        beam.setRadiate(radiate);
    }

    public void direction(Direction direction) {
        beam.setDirection(direction);
    }

}
