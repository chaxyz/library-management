<script setup>
import { RouterView } from 'vue-router'
import ToastPopup from '@/components/ToastPopup.vue'
</script>

<template>
  <ToastPopup />
  <router-view></router-view>
</template>
