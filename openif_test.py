# imports
import sys
import getopt
import requests # pip install requests


# ---

ACCOUNT      = 'account'
CERTIFICATE  = 'certificate'
REGISTRATION = 'registration'
ENVIRONMENT  = 'environment'


# open interface vars
OPENIF_ADDRESS  = 'https://openif.' + ENVIRONMENT + '.' + ACCOUNT + '.mot.test-service.org.uk/'
OPENIF_ENDPOINT = 'dvla/servlet/ECSODDispatcher?VRM=' + REGISTRATION


# environments
DEV_ENV  = "dev"
PRV_ENV  = "prv"
PREP_ENV = "prep"


# environment accounts
DEV_ACC  = 'dev'
LIVE_ACC = 'live'
ENV_ACCOUNTS = {DEV_ENV: DEV_ACC, PRV_ENV: DEV_ACC, PREP_ENV: LIVE_ACC}

# ---


# return a list of all supported environments
def getSupportedEnvironments():
    supported_environments = [PRV_ENV, PREP_ENV]

    for i in range (1,21):
        supported_environments.append(DEV_ENV + str(i).zfill(2))

    return supported_environments


# return arguments passed into the script
def getArgs(argv):
    environment  = ''
    registration = ''
    certificate = ''

    try:
        opts, args = getopt.getopt(argv[1:],'he:r:c:',['help', 'environment=', 'registration=', 'certificate='])

    except getopt.GetoptError:
        print('openif_test.py -e <environment> -r <registration> -c <certificate>')
        sys.exit(2)

    for opt, arg in opts:
        if opt == '-h':
            print('openif_test.py -e <environment> -r <registration> -c <certificate>')
            sys.exit()

        elif opt in ('-e', '--environment'):
            environment = arg

        elif opt in ('-r', '--registration'):
            registration = arg

        elif opt in ('-c', '--certificate'):
            certificate = arg

    return [environment,registration, certificate]


# get account for the environment
def getAccount(environment):
    if DEV_ENV in environment:
        return DEV_ENV

    elif PRV_ENV in environment:
        return PRV_ENV

    elif PREP_ENV in environment:
        return PREP_ENV


# open interface url to send request to
def getRequestURL(environment, registration):
    account = ENV_ACCOUNTS[getAccount(environment)]

    return OPENIF_ADDRESS.replace(ENVIRONMENT, environment).replace(ACCOUNT, account)
    + OPENIF_ENDPOINT.replace(REGISTRATION, registration)


# get vehicle info from open interface
def main(argv):
    args = getArgs(argv)

    if args[0] not in getSupportedEnvironments():
        print('Unsupported environment: ' + args[0])
        sys.exit(2)

    request = requests.get(getRequestURL(args[0], args[1]), verify=args[2])

    print(request.text)

main(sys.argv)
