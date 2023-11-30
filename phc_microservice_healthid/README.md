# Overview 📝

The Health ID is used for the purposes of uniquely identifying persons, authenticating them, and threading their health records (only with the informed consent of the patient) across multiple systems and stakeholders. As per the Health Information Provider (HIP) guidelines, healthcare providers use the Health IDs to store, process and update health records. 

Health IDs can be created by either self-registration or in an assisted manner at a healthcare provider or other authorized entities. This microservice will orchestrate the "Assisted Registration”.  Assisted Registration is a bunch of Javascript based APIs that can be leveraged to generate a HelathId for a citizen. 

This service is used by Healthcare providers to assist patients in creating Health IDs by integrating with the assisted registration APIs. The flows - creation with or without Aadhar is taken care by the assisted registration.
