// import the `Kafka` instance from the kafkajs library
const { Kafka } = require("kafkajs");
const { v4: uuidv4 } = require("uuid");

// the client ID lets kafka know who's producing the messages
const clientId = "survey-svc";
// we can define the list of brokers in the cluster
let brokers = [];
if (process.env.NODE_ENV == "test" || process.env.profile == "local") {
  brokers.push("localhost:9092");
} else {
  brokers = process.env.KAFKA_BROKERS.split(",");
}
// initialize a new kafka client and initialize a producer from it
const kafka = new Kafka({ clientId, brokers });

const producer = kafka.producer();

// we define an async function that writes a new message each second
const produce = async (param) => {
  let uuid = uuidv4();
  let authHeader = param.authHeader;
  let domainObject = param.domainObject;
  let idToken = param.idToken;
  let topic = process.env.profile + "_" + param.topic;
  await producer.connect();

  let domainProbe = {
    context: {
      authToken: authHeader,
      language: "en",
    },
    type: topic,
    guid: uuid,
    actor: {
      id: idToken,
      hasMembership: {
        role: null,
        organization: null,
      },
    },
    object: domainObject,
    eventTime: new Date().toISOString(),
    instrument: clientId,
  };

  await producer.send({
    topic,
    messages: [
      {
        key: topic,
        value: JSON.stringify(domainProbe),
      },
    ],
  });
};

module.exports = { produce: produce };
