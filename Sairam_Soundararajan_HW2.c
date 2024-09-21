#include <stdio.h>
#include <unistd.h>
#include <sys/wait.h>

int main()
{
int pid, pidGP , GPpid, status, status2;
pidGP = fork();
GPpid = getppid();
switch (pidGP)
{
    case -1: // An error has occured during the fork process.
        printf("Fork error.");
        break;
    case 0:
        pid = fork();
        switch (pid)
        {
          case -1: 
            printf("Fork error.");
            break;
          case 0: // executed by child process
             printf("I am the child process and my pid is X. My parent P has pid Y. My grandparent G has pid Z.\n", getpid(), getppid(), GPpid);
             break;
          default:
             wait(&status);
             printf("I am the parent process P and my pid is Y. My parent G has pid Z.");
             printf("\n");
             break; 
          }
          break;
  default:
    wait(&status2);
    printf("I am the grandparent process G and my pid is Z.");
    break;
  }
  }
        


