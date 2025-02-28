# Sử dụng Python 3.10 làm base image
FROM python:3.10

# Đặt thư mục làm việc trong container
WORKDIR /app

# Sao chép file requirements.txt vào container
COPY requirements.txt .

# Cài đặt các thư viện cần thiết
RUN pip install --no-cache-dir -r requirements.txt

# Sao chép toàn bộ mã nguồn vào container
COPY . .

ENV API_KEY=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
ENV GEMINI_KEY=AIzaSyDJsyCayAG8pCRVt_eTGMcK1h0cf5tcPws

# Expose cổng mà FastAPI sẽ chạy
EXPOSE 8084

# Chạy ứng dụng FastAPI với Uvicorn
CMD ["uvicorn", "app.app:app", "--host", "0.0.0.0", "--port", "8084"]
