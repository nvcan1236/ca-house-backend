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


# Expose cổng mà FastAPI sẽ chạy
EXPOSE 8084

# Chạy ứng dụng FastAPI với Uvicorn
CMD ["uvicorn", "app.app:app", "--host", "0.0.0.0", "--port", "8084"]
