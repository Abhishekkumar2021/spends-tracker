import { Injectable, Logger } from '@nestjs/common';

@Injectable()
export class LoggingService {
  private readonly logger = new Logger(LoggingService.name);

  log(message: string, context?: string) {
    if (context) {
      this.logger.log(message, context);
    } else {
      this.logger.log(message);
    }
  }

  error(message: string, stack?: string, context?: string) {
    if (context) {
      this.logger.error(message, stack, context);
    } else if (stack) {
      this.logger.error(message, stack);
    } else {
      this.logger.error(message);
    }
  }

  warn(message: string, context?: string) {
    if (context) {
      this.logger.warn(message, context);
    } else {
      this.logger.warn(message);
    }
  }

  debug(message: string, context?: string) {
    if (context) {
      this.logger.debug(message, context);
    } else {
      this.logger.debug(message);
    }
  }
}
