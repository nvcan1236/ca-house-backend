import textwrap

from pydantic import BaseModel

from app.models.post import PostType

API_KEY = "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"
GEMINI_KEY = "AIzaSyCcmDTmsbNQgoDhocqGalsWfRFKOSn7eKY"
import google.generativeai as genai
from IPython.display import Markdown

# from google.colab import userdata
# GOOGLE_API_KEY=userdata.get('GOOGLE_API_KEY')
genai.configure(api_key=GEMINI_KEY)


# openai.api_key = API_KEY

# def suggest_post_content():
#     return openai.chat.completions.create(
#         model="gpt-3.5-turbo",
#         messages=[
#             {
#                 "role": "user",
#                 "content": "Viết nội dung cho một bài viết tìm kiếm trọ ở Hồ Chí Minh."
#             }
#         ]
#     )
class Requirement(BaseModel):
    location: str = None
    area: float = None
    budget: float = None
    amenity: str = None
    post_type: PostType


def to_markdown(text):
    text = text.replace('•', '  *')
    return Markdown(textwrap.indent(text, '> ', predicate=lambda _: True))


def get_prompt(requirement: Requirement):
    if requirement.post_type.__eq__(PostType.FIND_ROOM):
        return (
            f"Tôi đang muốn tìm trọ. Viết một bài đăng ngắn khoảng 70-100 từ giúp tôi tìm trọ với các yêu cầu sau: "
            f"-Vị trí: {requirement.location}, -Diện tích: {requirement.area}, -Ngân sách: {requirement.budget}, "
            f"-yêu câu tiện nghi khác: {requirement.amenity}. Các trường bằng null có thể bỏ qua. "
            f"Nội dung hoàn chỉnh nếu thiếu có thể bỏ qua")
    elif requirement.post_type.__eq__(PostType.PASS_ROOM):
        return (
            f"Tôi đang muốn chuyển nhượng trọ cho người khác. Viết một bài đăng ngắn khoảng 70-150 từ giúp tôi nhượng "
            f"trọ với các đặc điểm sau:"
            f"-Vị trí: {requirement.location}, -Diện tích: {requirement.area}, -Giá thuê: {requirement.budget}, "
            f"-yêu câu tiện nghi khác: {requirement.amenity}. Các trường bằng null có thể bỏ qua. "
            f"Nội dung hoàn chỉnh nếu thiếu có thể bỏ qua")
    elif requirement.post_type.__eq__(PostType.REVIEW):
        return (
            f"Tôi đang muốn review trọ. Viết một bài đăng ngắn khoảng 100-150 từ giúp tôi review trọ với các đặc điểm "
            f"sau:-Vị trí: {requirement.location}, -Diện tích: {requirement.area}, -Giá thuê: {requirement.budget}, "
            f"-yêu câu tiện nghi khác: {requirement.amenity}. Các trường bằng null có thể bỏ qua. "
            f"Nội dung hoàn chỉnh nếu thiếu có thể bỏ qua")
    elif requirement.post_type.__eq__(PostType.FIND_ROOMMATE):
        return (
            f"Tôi đang muốn người ở chung trọ. Viết một bài đăng ngắn khoảng 100-150 từ giúp tôi tìm người ở chung "
            f"trọ trọ với các đặc điểm"
            f"sau:-Vị trí: {requirement.location}, -Diện tích: {requirement.area}, -Giá thuê: {requirement.budget}, "
            f"-yêu câu tiện nghi khác: {requirement.amenity}. Các trường bằng null có thể bỏ qua. "
            f"Nội dung hoàn chỉnh nếu thiếu có thể bỏ qua")


def suggest_post_content(requirement):
    model = genai.GenerativeModel('gemini-1.5-flash')
    prompt = get_prompt(requirement)

    try:
        response = model.generate_content(prompt)
        return to_markdown(response.text)
    except Exception as e:
        return f"Lỗi xảy ra: {str(e)}"
