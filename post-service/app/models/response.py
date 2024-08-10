from typing import TypeVar

T = TypeVar('T')


def ApiResponse(code, result: T, message):
    return {
        "code": code,
        "result": result,
        "message": message
    }
