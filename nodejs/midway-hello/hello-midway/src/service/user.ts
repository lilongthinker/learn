import { Provide, Inject } from '@midwayjs/decorator';
import { IUserOptions } from '../interface';
import { ILogger } from '@midwayjs/logger';
@Provide()
export class UserService {
  @Inject()
  logger: ILogger;

  async getUser(options: IUserOptions) {
    this.logger.info('options: '+JSON.stringify(options));

    return {
      uid: options.uid,
      username: 'mockedName',
      phone: '12345678901',
      email: 'midway-mock@163.com',
    };
  }
}
