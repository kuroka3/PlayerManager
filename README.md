# PlayerManager
PlayerManager is a plugin to moderate players in server

# 한국어 / Korean
플레이어 관리를 위해 만든 플러그인

독자적인 시스템을 사용합니다.

※ 이 플러그인은 Gradle을 사용합니다.

※ 이 플러그인에는 권한 시스템이 있습니다. 권한 관리를 위해 Luckperms등 다른 플러그인을 사용해 주십시오.

※ 이 플러그인은 1.19.4 버전만 지원합니다. 정식버전 출시 시 1.20 버전을 지원할 예정입니다.

## PlayerManager-old와 다른 점
 - 더 이상 GSON 라이브러리를 사용하지 않습니다. 
 - 이제 json-simple 라이브러리를 사용합니다.
 - 실시간으로 파일이 저장됩니다.
 - 코드가 최적화 되었습니다.

## 구현 예정 기능: 정식버전까지 모든기능 구현이 목표
 - 유저 밴/경고/뮤트/킥 | 언밴/언경고(경고취소)/언뮤트 🟨
 - 권한 🟥
 - 관리 로그 🟨
 - 밴id 🟨
 - Case 시스템 🟥
 - 시간제 밴 🟥
 - 90일 이후 경고 자동제거 🟥
 - 언어 설정 🟥

# 영어 / English
A plugin I made to moderate player

uses own system.

※ This plugin uses Gradle.

※ This plugin has permission system. Use luckperms(or etc.) to give/take permission.

※ This plugin supports ONLY 1.19.4. I plan to support 1.20 version when the main release is released.

## Difference from PlayerManager-old
 - No longer use GSON library.
 - Now use json-simple library.
 - JSON File is saved in real time.
 - Code optimized.

## Features: I'll make these features until release.
 - User ban/warn/mute/kick | unban(pardon)/unwarn/unmute 🟨
 - Permissions 🟥
 - Moderation log 🟨
 - Ban ID 🟨
 - Case System 🟥
 - Period ban 🟥
 - Automatic unwarn after 90d 🟥
 - Support two or more Language 🟥
