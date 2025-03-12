<template>
    <HeaderBar
      :currentName="_t18('reserveProof.title')"
    ></HeaderBar>
    <div class="mobile-container">
        <section class="banner-section">
            <div class="banner-content-wrapper">
                <h1 class="banner-title">{{ $t('reserveProof.title') }}</h1>
                <p class="banner-desc">
                    {{ $t('reserveProof.content') }}
                </p>
            </div>
            <div class="video-wrapper">
                <video 
                    class="banner-video" 
                    :title="$t('reserveProof.videoTitle')" 
                    controls 
                    muted
                    playsinline 
                    poster="https://www.okx.com/cdn/assets/imgs/2311/E2656D8B0DC7D3C4.png"
                    id="porVideo"
                >
                    <source src="https://www.okx.com/cdn/assets/files/2311/6D88CDAE0D1CCC45.mp4" type="video/mp4">
                </video>
            </div>
        </section>

        <section class="info-section">
            <h2 class="section-title">{{ $t('reserveProof.processTitle') }}</h2>
            <div class="section-content">
                <p>{{ $t('reserveProof.processDescription') }}</p>
                <ol class="step-list">
                    <li>{{ $t('reserveProof.step1') }}</li>
                    <li>{{ $t('reserveProof.step2') }}</li>
                    <li>{{ $t('reserveProof.step3') }}</li>
                </ol>
            </div>
        </section>

        <section class="info-section">
            <h2 class="sub-title">{{ $t('reserveProof.trustTitle') }}</h2>
            <p class="banner-content">
                {{ $t('reserveProof.trustDescription') }}
            </p>
            <p class="banner-content">
                {{ $t('reserveProof.feedback') }}
            </p>
        </section>

        <section class="info-section">
            <h2 class="sub-title">{{ $t('reserveProof.commitmentTitle') }}</h2>
            <p class="banner-content">
                {{ $t('reserveProof.commitmentDescription') }}
            </p>
            <p class="banner-content">
                {{ $t('reserveProof.updateCommitment') }}
            </p>
        </section>

        <section class="info-section">
            <h2 class="sub-title">{{ $t('reserveProof.advantagesTitle') }}</h2>
            <p class="banner-content">
                - {{ $t('reserveProof.advantageTransparency') }}
            </p>
            <p class="banner-content">
                - {{ $t('reserveProof.advantageSecurity') }}
            </p>
            <p class="banner-content">
                - {{ $t('reserveProof.advantageConvenience') }}
            </p>
        </section>

        <section class="info-section">
            <h2 class="sub-title">{{ $t('reserveProof.faqTitle') }}</h2>
            <p class="banner-content">
                <strong>{{ $t('reserveProof.faq1.question') }}</strong><br>
                {{ $t('reserveProof.faq1.answer') }}
            </p>
            <p class="banner-content">
                <strong>{{ $t('reserveProof.faq2.question') }}</strong><br>
                {{ $t('reserveProof.faq2.answer') }}
            </p>
        </section>
    </div>
</template>

<script>
export default {
    // 这里可以添加其他逻辑
}
</script>

<style scoped>
.mobile-container {
    padding: 16px 24px;
    background: #000;
    color: #fff;
}

.banner-section {
    padding: 20px 16px;
    margin: 0 -24px;
    background: linear-gradient(135deg, #000, #333);
}

.banner-content-wrapper {
    margin-bottom: 20px;
    padding: 0 8px;
}

.banner-title {
    font-size: 24px;
    font-weight: bold;
    margin-bottom: 12px;
    text-align: left;
}

.banner-desc {
    font-size: 14px;
    line-height: 1.5;
    margin-bottom: 20px;
    text-align: left;
}

.video-wrapper {
    width: 100%;
    margin: 0 auto;
    padding: 0 8px;
}

.banner-video {
    width: 100%;
    height: auto;
    border-radius: 8px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.3);
}

.info-section {
    padding: 20px 8px;
    border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.section-title {
    font-size: 20px;
    font-weight: bold;
    margin-bottom: 16px;
    text-align: left;
}

.section-content {
    font-size: 14px;
    line-height: 1.6;
    text-align: left;
}

.step-list {
    padding-left: 20px;
    margin: 16px 0;
}

.step-list li {
    margin-bottom: 12px;
}

.sub-title {
    font-size: 24px;
    font-weight: bold;
    color: #fff;
    margin-top: 20px;
}

.banner-content {
    text-align: left;
    font-size: 14px;
    margin-top: 20px;
    color: #fff;
    max-width: 100%;
    padding: 0 8px;
}

.button-bar {
    display: flex;
    justify-content: center;
    gap: 10px;
}

.btn {
    padding: 10px 20px;
    border: none;
    cursor: pointer;
}

.btn-primary {
    background-color: #fff;
    color: #000;
}

.btn-outline {
    background-color: transparent;
    border: 1px solid #fff;
    color: #fff;
}

/* 移除之前的悬停效果，在移动端不需要 */
.banner-item:hover,
.info-section:hover {
    transform: none;
}

/* 移除之前的网格布局相关样式 */
.banner-grid,
.banner-item {
    display: block;
    margin: 0;
    text-align: left;
}
</style>
