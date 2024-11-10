import { HttpStatus } from '@nestjs/common';
import { AxiosResponse } from 'axios';
import { firstValueFrom, Observable } from 'rxjs';
import { GlobalException } from 'src/exceptions/global.exception';
import { ApiResponse } from 'src/interfaces/api-response.interface';
import { ErrorResponse } from 'src/interfaces/error-response.interface';

export async function requestHandler(
  requestObservable: Observable<AxiosResponse<ApiResponse, any>>,
): Promise<ApiResponse> {
  try {
    const response = await firstValueFrom(requestObservable);
    return response.data; // Return the response data
  } catch (error) {
    if (error.response) {
      const errorResponse: ErrorResponse = error.response.data;
      throw new GlobalException(
        errorResponse.message,
        errorResponse.statusCode,
      );
    } else {
      throw new GlobalException(
        'Something went wrong',
        HttpStatus.INTERNAL_SERVER_ERROR,
      );
    }
  }
}
