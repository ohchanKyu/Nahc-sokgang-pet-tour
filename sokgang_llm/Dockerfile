FROM python:3.9-slim

WORKDIR /app

COPY sokgang_llm/requirements.txt ./requirements.txt
RUN pip install --no-cache-dir -r requirements.txt

COPY sokgang_llm/ .

EXPOSE 5000

CMD ["uvicorn", "app:app", "--host", "0.0.0.0", "--port", "5000"]
