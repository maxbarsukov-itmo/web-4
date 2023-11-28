import axios from "axios";
import apiUrl from "@/config/constants";

const api = axios.create({ baseURL: `${apiUrl}/api` });
export default api;

export function addPoint(data, config, then, err) {
  api.post('/attempts', data, config)
    .then(response => then(response.data))
    .catch(error => err(error.response.data));
}

export function getPoints(data, then, err) {
  run(api.get, '/attempts', data, then, err);
}

export function deletePoints(data, then, err) {
  run(api.delete, '/attempts', data, then, err);
}

export function registerApi(data, then, err) {
  run(api.post, '/users/register', data, then, err);
}

export function loginApi(data, then, err) {
  run(api.post, '/users/login', data, then, err);
}

function run(method, url, data, then, err) {
  method(url, data)
    .then(response => then(response.data))
    .catch(error => err(error.response.data));
}
