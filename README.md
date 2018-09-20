# Conflict Detector

## 주의사항
반드시 ./limboole/limboole.exe가 같이 있어야 함!

## TO DO List

1. 라이브러리로 패키징해서 Platform Manager나 Policy Editor로 이식하기.
1.1. JavaMain.java에서 Conflict Detector를 어떻게 사용하는지 참고할 것.
1.2. 주로 사용하는 클래스는 

2. 출력 형식 유저도 이해하기 쉽게 수정하기
2.1. 이건 Policy Editor에서 해야할 일이라고 생각됨.

3. (Optional) 여러 정책 파일간의 충돌도 탐지하기.
3.1. 현재는 Policy Set까지 가능하긴 하지만, 하나의 파일에 다 들어있는 경우만 가능함.
3.2. 만약에 PolicySet에 외부링크로 다른 정책 파일을 참조하는 경우 그것까지 읽을 수 있도록 하면 완벽해짐.
3.3. 구현하기 위해서는 Detector.scala에서 convertPolicy나 loadPolicy를 수정할 것.
3.3.1. PolicySet 파일을 읽었다면,
3.3.2. PolicySet 파일 안쪽의 정책 파일 참조 태그를 읽어서
3.3.3. 그 파일 내용 그대로 해당 태그 치환해버리기.