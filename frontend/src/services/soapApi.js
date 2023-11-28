import apiUrl from "@/config/constants";
import api from "@/services/restApi";
import { XMLParser, XMLValidator } from "fast-xml-parser";

const url = `${apiUrl}/ws`;

function parseXml(xmlData) {
  const options = {
    attributeNamePrefix : "ns2",
    ignoreAttributes : false,
    ignoreNameSpace: true,
    removeNSPrefix: true,
  };
  const parser = new XMLParser(options);
  return parser.parse(xmlData);
}

function parseSoapResponse(xmlData) {
  const json = parseXml(xmlData)
  return json.Envelope.Body;
}

export default function soapApi(query, responseName, then, err) {
  const request =  `
    <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                    xmlns:gs="http://itmo.ru/web/lab4">
      <soapenv:Header/>
      <soapenv:Body>${query}</soapenv:Body>
    </soapenv:Envelope>
  `;

  api.post(url, request, { headers: { 'Content-Type': 'text/xml' } })
    .then(res => {
      const json = parseSoapResponse(res.data)[responseName];
      if (json.code === 200 || json.code === 201) {
        then(json);
        return;
      }
      err(json);
    })
    .catch(error => {
      console.error(error);

      if (error.response?.data) {
        const json = parseSoapResponse(error.response?.data);
        if (json[[responseName]]) return err(json[responseName]);
        return err(json)
      }
    });
}

export function addPoint(data, token, then, err) {
  const query =  `
        <gs:addPointRequest>
            <gs:token>${token}</gs:token>
            <gs:attemptDto>
                <gs:x>${data.x}</gs:x>
                <gs:y>${data.y}</gs:y>
                <gs:r>${data.r}</gs:r>
            </gs:attemptDto>
        </gs:addPointRequest>
  `;
  return soapApi(query, 'addPointResponse', then, err);
}

export function getPoints(token, then, err) {
  const query =  `
    <gs:getPointsRequest>
        <gs:token>${token}</gs:token>
    </gs:getPointsRequest>
  `;
  return soapApi(query, 'getPointsResponse', then, err);
}

export function deletePoints(token, then, err) {
  const query =  `
    <gs:deletePointsRequest>
        <gs:token>${token}</gs:token>
    </gs:deletePointsRequest>
  `;
  return soapApi(query, 'deletePointsResponse', then, err);
}

export function registerApi({ email, password }, then, err) {
  const query =  `
    <gs:registerRequest>
        <gs:userDto>
            <gs:email>${email}</gs:email>
            <gs:password>${password}</gs:password>
        </gs:userDto>
    </gs:registerRequest>
  `;
  return soapApi(query, 'registerResponse', then, err);
}

export function loginApi({ email, password }, then, err) {
  const query =  `
    <gs:loginRequest>
        <gs:userDto>
            <gs:email>${email}</gs:email>
            <gs:password>${password}</gs:password>
        </gs:userDto>
    </gs:loginRequest>
  `;
  return soapApi(query, 'loginResponse', then, err);
}
