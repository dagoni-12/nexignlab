import requests

generate_db = "http://localhost:8080/api/generate"
generate_report_url = "http://localhost:8080/api/generate-report"

requests.post(generate_db)
data = {"msisdn": "79995556677", "startTime": "2024-06-01T12:00:00", "endTime": "2024-07-01T17:50:00"}
requests.post(generate_report_url, params=data)