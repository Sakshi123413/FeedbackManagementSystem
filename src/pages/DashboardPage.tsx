import { useState, useEffect } from "react"
import { Navbar } from "@/components/Navbar"
import { FeedbackForm } from "@/components/FeedbackForm"
import { FeedbackTable } from "@/components/FeedbackTable"
import { Card, CardContent } from "@/components/ui/card"
import { getFeedbacks, type Feedback } from "@/services/feedbackService"
import { MessageSquare, Star, TrendingUp, Users } from "lucide-react"

export function DashboardPage() {
  const [refreshKey, setRefreshKey] = useState(0)
  const [stats, setStats] = useState({ total: 0, avgRating: 0, topRated: 0, users: 0 })

  useEffect(() => {
    async function loadStats() {
      const feedbacks: Feedback[] = await getFeedbacks()
      const total = feedbacks.length
      const avgRating = total > 0
        ? Number((feedbacks.reduce((sum, f) => sum + f.rating, 0) / total).toFixed(1))
        : 0
      const topRated = feedbacks.filter(f => f.rating >= 4).length
      const users = new Set(feedbacks.map(f => f.userName)).size
      setStats({ total, avgRating, topRated, users })
    }
    loadStats()
  }, [refreshKey])

  const handleFeedbackSubmitted = () => {
    setRefreshKey(prev => prev + 1)
  }

  return (
    <div className="min-h-screen bg-background">
      <Navbar />
      <main className="container py-8">
        {/* Stats Cards */}
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4 mb-8">
          <StatCard
            icon={<MessageSquare className="h-5 w-5" />}
            label="Total Feedback"
            value={stats.total.toString()}
          />
          <StatCard
            icon={<Star className="h-5 w-5" />}
            label="Average Rating"
            value={stats.avgRating.toString()}
          />
          <StatCard
            icon={<TrendingUp className="h-5 w-5" />}
            label="Positive Reviews"
            value={stats.topRated.toString()}
          />
          <StatCard
            icon={<Users className="h-5 w-5" />}
            label="Unique Users"
            value={stats.users.toString()}
          />
        </div>

        {/* Main Content */}
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
          <div className="lg:col-span-1">
            <FeedbackForm onSubmitted={handleFeedbackSubmitted} />
          </div>
          <div className="lg:col-span-2">
            <FeedbackTable refreshKey={refreshKey} />
          </div>
        </div>
      </main>
    </div>
  )
}

function StatCard({ icon, label, value }: { icon: React.ReactNode; label: string; value: string }) {
  return (
    <Card className="hover:shadow-card-hover">
      <CardContent className="p-5">
        <div className="flex items-center gap-4">
          <div className="h-11 w-11 rounded-xl bg-accent flex items-center justify-center text-accent-foreground">
            {icon}
          </div>
          <div>
            <p className="text-2xl font-bold text-foreground">{value}</p>
            <p className="text-xs text-muted-foreground">{label}</p>
          </div>
        </div>
      </CardContent>
    </Card>
  )
}
