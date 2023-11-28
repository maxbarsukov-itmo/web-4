import apiUrl from "@/config/constants";
import api from "@/services/restApi";

const url = `${apiUrl}/ws`;

function soapApi(query, then, err) {
  const request =  `
    <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                    xmlns:gs="http://itmo.ru/web/lab4">
      <soapenv:Header/>
      <soapenv:Body>${query}</soapenv:Body>
    </soapenv:Envelope>
  `;

  api.post(url, request, { headers: { 'Content-Type': 'text/xml' } })
    .then(res => then(res.data))
    .catch(error => err(error.response.data));
}

export function addPoint(data, token, then, err) {
  const query =  `
        <gs:addPointRequest>
            <gs:token>${token}</gs:token>
            <gs:attemptDto x="${data.x}" y="${data.y}" r="${data.r}"/>
        </gs:addPointRequest>
  `;
  soapApi(query, then, err);
}

export function getPoints(token, then, err) {
  const query =  `
    <gs:getPointsRequest>
        <gs:token>${token}</gs:token>
    </gs:getPointsRequest>
  `;
  soapApi(query, then, err);
}

export function deletePoints(token, then, err) {
  const query =  `
    <gs:deletePointsRequest>
        <gs:token>${token}</gs:token>
    </gs:deletePointsRequest>
  `;
  soapApi(query, then, err);
}

function registerApi({ email, password }, then, err) {
  const query =  `
    <gs:registerRequest>
        <gs:userDto email="${email}" password="${password}"/>
    </gs:registerRequest>
  `;
  soapApi(query, then, err);
}

function loginApi({ email, password }, then, err) {
  const query =  `
    <gs:loginRequest>
        <gs:userDto email="${email}" password="${password}"/>
    </gs:loginRequest>
  `;
  soapApi(query, then, err);
}
