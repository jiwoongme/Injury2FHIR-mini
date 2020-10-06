package com.company;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.r4.model.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Injury2FHIR {

    //make patient resource
    public String makePatient(String Identifier, String gender, double age) {

        Patient patient = new Patient();
        //set ID
        patient.addIdentifier().setId(Identifier);

        //set Gender
        if (gender.compareTo("C1") == 0) {
            patient.setGender(Enumerations.AdministrativeGender.MALE);
        } else {
            patient.setGender(Enumerations.AdministrativeGender.FEMALE);
        }
        //set age
        Extension extension = new Extension();
        extension.setUrl("http://hl7.org/fhir/ValueSet/age-units");
        extension.setValue(new Age().setValue(age));
        patient.addExtension(extension);

        //Resource To Json
        FhirContext ctx = FhirContext.forR4();

        return ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(patient);
    }

    //make encounter resource
    public String makeEncounter(String PatientIdentifier, String inDate) {

        Encounter encounter = new Encounter();
        //set status
        encounter.setStatus(Encounter.EncounterStatus.FINISHED);

        //set class
        encounter.setClass_(encounter.getClass_().setCode("EMER").setDisplay("emergency"));

        //set subject
        encounter.setSubject(new Reference().setReference("Patient/"+PatientIdentifier));

        //set period
        Period period = new Period();
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyyMMdd HHmmss");
        Date startDate = null;
        try {
            startDate = transFormat.parse(inDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        period.setStart(startDate);
        encounter.setPeriod(period);

        //Resource To Json
        FhirContext ctx = FhirContext.forR4();

        return ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(encounter);
    }
}
