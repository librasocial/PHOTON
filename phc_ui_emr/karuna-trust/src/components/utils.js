export function getHostEnvironment() {
  let arr = ["localhost", "stg", "dev"];
  let getEnv = "";
  const hostname = window && window.location && window.location.hostname;

  for (let env of arr) {
    if (window.location.hostname.includes(env) === true) {
      getEnv = env;
    }
  }

  return getEnv;
}
