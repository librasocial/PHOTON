// import the `Kafka` instance from the kafkajs library
const { Kafka } = require("kafkajs");
const { v4: uuidv4 } = require("uuid");

// the client ID lets kafka know who's producing the messages
const clientId = "PHC_HealthId";
// we can define the list of brokers in the cluster
let brokers = [];
if (process.env.NODE_ENV == "test") {
  brokers.push("localhost:9092");
} else {
  brokers = process.env.KAFKA_BROKERS.split(",");
}
console.log(brokers);
// initialize a new kafka client and initialize a producer from it
const kafka = new Kafka({ clientId, brokers });

let topic = process.env.profile + "_HealthId";

const producer = kafka.producer();

// we define an async function that writes a new message each second
const produce = async (param) => {
  let uuid = uuidv4();
  let authHeader = param.authHeader;
  let domainObject = param.domainObject;
  let idToken = param.idToken;
  let type = param.type;

  await producer.connect();

  let domainProbe = {
    context: {
      authToken: authHeader,
      language: "en",
    },
    type: type,
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
        key: type,
        value: JSON.stringify(domainProbe),
      },
    ],
  });
};

module.exports = { produce: produce };
