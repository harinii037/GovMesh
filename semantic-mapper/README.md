# GovMesh – AI Semantic Mapper

## Overview

The Semantic Mapper is the AI component of GovMesh responsible for identifying meaningful relationships between fields in two different schemas.

Different government platforms may use different field names for the same information.

For example:

- `citizen_name` → `applicantName`
- `beneficiary_earnings` → `annualIncome`
- `person_birth_info` → `dateOfBirth`
- `contact_phone` → `mobileNumber`

The Semantic Mapper uses the Gemini LLM to understand the meaning of fields and generate semantic mapping suggestions.

---

## M3 Responsibility

M3 is responsible for:

- AI-based semantic schema mapping
- Gemini LLM integration
- Schema validation
- Confidence score generation
- Mapping status classification
- AI response validation
- REST API implementation
- Error handling
- Health check endpoint
- Supporting M1/M2 during integration

---

## Problem

Different applications may represent the same information using different:

- Field names
- Naming conventions
- Abbreviations
- Terminology

For example:

```text
Source Schema          Target Schema

citizen_name      →    applicantName
date_of_birth     →    dateOfBirth
mobile_number     →    phoneNumber

Solution

The Semantic Mapper receives a source schema and target schema through an API.

It sends the schemas to the Gemini LLM, which analyzes the semantic meaning of the fields and suggests appropriate mappings.

The Python service then validates the AI response and returns:

Source field
Target field
Confidence score
Mapping status

AI Architecture
Source Schema
      +
Target Schema
      |
      v
POST /semantic-map
      |
      v
FastAPI
      |
      v
LLM Mapper
      |
      v
Gemini AI
      |
      v
Semantic Analysis
      |
      v
Result Validation
      |
      v
Confidence + Status
      |
      v
JSON Response
Why camelCase and snake_case Normalization Matters

Different applications commonly use different naming conventions.

snake_case
citizen_name
date_of_birth
mobile_number
camelCase
citizenName
dateOfBirth
mobileNumber

Although the names are different, they can represent the same information.

Handling these naming differences helps the Semantic Mapper identify relationships based on meaning instead of exact spelling.

Why Semantic Matching is Used

Exact string matching is not sufficient for interoperability.

For example:

beneficiary_earnings

and:

annualIncome

have different names but represent related information.

Similarly:

person_birth_info

and:

dateOfBirth

represent the same concept.

Semantic matching allows the AI to understand the meaning of fields and identify relationships even when their names are different.

Why This is AI and Not a Lookup Table

The Semantic Mapper does not contain a hardcoded mapping table such as:

citizen_name → applicantName
mobile_number → phoneNumber
date_of_birth → dateOfBirth

Instead, the source and target schemas are dynamically provided to the Gemini LLM.

The model analyzes the field names and their meanings and generates mapping suggestions.

This allows the system to handle previously unseen field names and schema combinations without manually adding every mapping.

Python is used to validate and control the AI-generated output before returning it to the client.

Confidence Scores

Each mapping contains a confidence score between 0 and 1.

The score represents how strongly the AI believes that the source and target fields are semantically related.

Mapping Classification
Confidence	Status	Meaning
>= 0.85	AUTO_MATCH	Strong semantic match
0.60 - 0.84	REVIEW	Possible match requiring review
< 0.60	REJECT	Weak or unsuitable match

Example:

{
  "source": "contact_phone",
  "target": "mobileNumber",
  "confidence": 0.92,
  "status": "AUTO_MATCH"
}

Confidence scores help distinguish reliable mappings from uncertain mappings.

Handling Unmappable Fields

The system does not force an incorrect mapping when no suitable target exists.

Example:

{
  "source": "favorite_color",
  "target": null,
  "confidence": 0.0,
  "status": "REJECT"
}

This allows the system to safely reject unrelated fields.

Design-Time AI

The AI is used during the schema mapping stage rather than during every runtime transaction.

Schema Discovery
       |
       v
AI Semantic Mapping
       |
       v
Mapping Suggestions
       |
       v
Human Approval
       |
       v
Mapping Contract
       |
       v
Runtime Data Exchange
Why Design-Time?

Using AI during design-time provides:

Lower runtime latency
Lower API cost
More predictable runtime behaviour
Reduced dependency on the AI service
Human review before deployment
Reusable mapping contracts

Once a mapping is approved, the runtime system can use the mapping contract instead of calling the LLM for every individual record.

API
POST /semantic-map

Generates semantic mappings between a source schema and a target schema.

Request
{
  "sourceSchema": {
    "citizen_name": "string",
    "date_of_birth": "string",
    "mobile_number": "string"
  },
  "targetSchema": {
    "applicantName": "string",
    "dateOfBirth": "string",
    "phoneNumber": "string"
  }
}
Response
{
  "suggestions": [
    {
      "source": "citizen_name",
      "target": "applicantName",
      "confidence": 0.92,
      "status": "AUTO_MATCH"
    },
    {
      "source": "date_of_birth",
      "target": "dateOfBirth",
      "confidence": 0.95,
      "status": "AUTO_MATCH"
    },
    {
      "source": "mobile_number",
      "target": "phoneNumber",
      "confidence": 0.89,
      "status": "AUTO_MATCH"
    }
  ]
}
GET /health

Checks whether the Semantic Mapper service is running.

Response
{
  "status": "UP"
}
Error Handling

Malformed requests are handled without crashing the service.

Invalid requests return:

400 Bad Request

Examples include:

Missing sourceSchema
Missing targetSchema
Empty schemas
Invalid field names
Invalid field data types
Invalid request structures

The service validates requests using Pydantic and converts validation errors into a clean 400 Bad Request response.

AI Response Validation

The Python service validates the response generated by Gemini.

Validation includes:

Source field must exist in the source schema.
Target field must exist in the target schema.
Confidence must be between 0 and 1.
Invalid targets are rejected.
A target can be null when no suitable mapping exists.
Mapping status is assigned based on confidence.

This provides a reliability layer between the AI model and the rest of GovMesh.

Technology Stack
Technology	Purpose
Python	Core application
FastAPI	REST API
Pydantic	Schema validation
Google Gemini	AI semantic mapping
Google GenAI SDK	Gemini integration
python-dotenv	Environment variable management
Uvicorn	Application server
Project Structure
semantic-mapper/
│
├── app/
│   ├── api/
│   │   └── routes.py
│   │
│   ├── models/
│   │   └── schemas.py
│   │
│   ├── services/
│   │   └── llm_mapper.py
│   │
│   └── main.py
│
├── .env
├── .gitignore
├── requirements.txt
└── README.md
Setup
1. Install Dependencies
pip install -r requirements.txt
2. Configure Gemini API

Create a .env file:

GEMINI_API_KEY=your_api_key

The API key must not be hardcoded or committed to GitHub.

Make sure .env is included in .gitignore.

3. Run the Service
uvicorn app.main:app --port 8000 --reload
4. Open Swagger
http://localhost:8000/docs
Integration with M1/M2

M1/M2 can use the Semantic Mapper through:

POST /semantic-map

The integration flow is:

M1 / M2
   |
   | Source Schema + Target Schema
   v
POST /semantic-map
   |
   v
M3 Semantic Mapper
   |
   v
Gemini AI
   |
   v
Mapping Suggestions
   |
   v
M1 / M2
   |
   v
Approve
   |
   v
Contract Creation

M3 provides the AI mapping service while M1/M2 can consume the mapping suggestions and continue with approval and contract creation.

Example
Source
{
  "beneficiary_earnings": "number",
  "person_birth_info": "string",
  "contact_phone": "string"
}
Target
{
  "annualIncome": "number",
  "dateOfBirth": "string",
  "mobileNumber": "string"
}
AI Mapping
beneficiary_earnings → annualIncome
person_birth_info    → dateOfBirth
contact_phone        → mobileNumber

Each mapping is returned with its confidence score and status.

Security

The Gemini API key is stored in an environment variable.

The API key should:

Never be hardcoded.
Never be committed to GitHub.
Never be included in the README.
Be stored securely in deployment environments.
Limitations

The current implementation depends on the semantic understanding of the AI model.

Potential limitations include:

Ambiguous field names may require human review.
Confidence scores are model-generated estimates.
Complex nested schemas may require additional processing.
AI API availability is required during mapping generation.
Domain-specific terminology may require additional context.
Future Improvements

Possible improvements include:

Human-in-the-loop approval interface
Mapping history
Mapping versioning
Audit logs
Automatic mapping contract generation
Nested schema support
Batch mapping
Improved confidence calibration
Domain-specific context
Production monitoring
Integration with the complete GovMesh contract workflow
M3 Summary

The M3 Semantic Mapper acts as the AI intelligence layer for schema interoperability in GovMesh.

Its main responsibility is to convert:

Source Schema + Target Schema

into:

Source Field
      ↓
Target Field
      ↓
Confidence
      ↓
Status

The system uses Gemini AI for semantic understanding, Python and Pydantic for validation, and FastAPI for exposing the mapping service.

The AI runs during design-time schema discovery, allowing approved mappings to be converted into contracts and reused during runtime data exchange.

The key advantage is that the system can understand new field names and relationships without depending on a manually maintained lookup table.